package com.skytech.moa.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.doc.widgets.FieldFile;
import com.skytech.moa.doc.widgets.attachment.FieldAttachment;
import com.skytech.android.draft.Draft;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.android.util.DisplayUtil;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class IssueDetailView implements View.OnClickListener, FieldFile.OnClickHappened {
    @Override
    public void onClickFile(boolean isMainBody, String fileId, String conid, String fileName) {
        dlg.onClickFile(fileId, conid, fileName, isMainBody);
    }

    public interface ClickHappened {
        public void onClickRefresh();

        public void onClickSaveDraft(JSONObject data);

        public void onClickSendNextStep(String action, JSONObject data);

        public void onClickFile(String fileId, String conId, String fileName, boolean isMainBody);

        public void onClickWorkStepMonitor();
    }

    private Activity act;
    private ClickHappened dlg;

    private TextView titleText;
    private View noNetwork;
    private ScrollView scroll;
    private ViewGroup body;
    private FormView form;
    private LinearLayout buttonBar;

    private int padding, spacing;

    public IssueDetailView(Activity activity, ClickHappened delegate) {
        this.act = activity;
        dlg = delegate;
        padding = (int) act.getResources().getDimension(R.dimen.main_content_margin);
        spacing = 10;//(int) act.getResources().getDimension(R.dimen.spacing);
        init();
    }

    private void init() {
        scroll = (ScrollView) act.findViewById(R.id.scroll);
        body = (ViewGroup) act.findViewById(R.id.body_issuecontent);
        form = (FormView) act.findViewById(R.id.form);
        titleText = (TextView) act.findViewById(R.id.title);
        buttonBar = (LinearLayout) act.findViewById(R.id.button_bar);
        noNetwork = act.findViewById(R.id.no_network);
        noNetwork.setOnClickListener(this);
        form.setParentScrollView(scroll);
    }

    /**
     * 显示流程日志按钮
     */
    public void showButtonWorkStepMonitor() {
        ImageView btn_proces = (ImageView) act.findViewById(R.id.search);
        btn_proces.setImageResource(R.drawable.ico_proces);
        btn_proces.setVisibility(View.VISIBLE);
        btn_proces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.onClickWorkStepMonitor();
            }
        });
    }

    public void setContentView(int resID) {
        if (-1 == resID) return;
        form.setContentView(resID);
        addButton("提　　交", Constant.DOC_BUTTON_ACTION_SUBMIT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_network:
                dlg.onClickRefresh();
                break;
        }
    }

    public void refresh(JSONObject data, Draft draft) {
        JSONObject draftJson = null;
        if (null != draft) {
            try {
                draftJson = new JSONObject(draft.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        titleText.setText(data.optString(Constant.DOC_TITLE));
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), draftJson);
        form.initAttachment(data.optJSONArray(Constant.DOC_ATTACHMENTS));
        body.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        CustomProgress.hideProgress();
    }

    public FormView getForm() {
        return form;
    }

    public void showMsg(String msg) {
        CustomToast.show(act, msg);
    }

    public void showMsg(int resId) {
        CustomToast.show(act, resId);
    }

    public void showError(String msg) {
        body.setVisibility(View.GONE);
        noNetwork.setVisibility(View.VISIBLE);
        CustomProgress.hideProgress();
        showMsg(msg);
    }

    private void addButton(String name, String action) {
        JSONObject json = new JSONObject();
        try {
            json.put(Constant.DOC_BUTTON_NAME, name);
            json.put(Constant.DOC_BUTTON_ACTION, action);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addButton(json);
    }

    private void addButton(JSONObject json) {
        if (null == json) return;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(spacing, padding, spacing, padding);
        Button button = new Button(act);
        button.setTag(json);
        String bName = json.optString(Constant.DOC_BUTTON_NAME);
        button.setText(bName.length() > 3 ? bName : "  " + bName + "  ");
        button.setBackgroundResource(R.drawable.button_blue);
        button.setSingleLine();
        button.setTextColor(act.getResources().getColor(R.color.white));
        button.getPaint().setFakeBoldText(true);
        button.setTextSize(DisplayUtil.getInstance(act).getDip(R.dimen.textsize_middle));
        buttonBar.addView(button, params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject json = (JSONObject) view.getTag();
                if (json.optString(Constant.DOC_BUTTON_ACTION).equalsIgnoreCase(Constant.DOC_BUTTON_ACTION_DRAFT)) {
                    //存草稿
                    JSONObject object = form.getFieldsData();
                    try {
                        object.put(Constant.DOC_ATTACHMENTS, form.getFieldAttachmentData());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dlg.onClickSaveDraft(object);
                } else if (form.validate()) {
                    //setEnabled(false);
                    //点击提交，先上传附件，附件上传成功后再提交表单
                    form.setUploadHandler(new FieldAttachment.UploadHandler() {
                        @Override
                        public void onUploadFailure() {
                            showMsg("附件上传失败");
                        }

                        @Override
                        public void onUploadSuccess() {
                            dlg.onClickSendNextStep(json.optString(Constant.DOC_BUTTON_ACTION), form.getFieldsData());
                        }
                    });
                }
            }
        });
    }

    public void initButtons(JSONArray array) {
        buttonBar.removeAllViews();
        if (null != array) {
            for (int i = 0; i < array.length() && i < 5; i++) {
                addButton(array.optJSONObject(i));
            }
        }
    }

    public void setEnabled(boolean enabled) {
        int viewCount = buttonBar.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            buttonBar.getChildAt(i).setEnabled(enabled);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        form.onActivityResult(requestCode, resultCode, data);
    }
}
