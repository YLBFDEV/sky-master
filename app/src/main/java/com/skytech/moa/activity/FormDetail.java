package com.skytech.moa.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.skytech.android.ArkActivity;
import com.skytech.android.draft.DraftManager;
import com.skytech.android.draft.IDraftManager;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.android.util.FileUtils;
import com.skytech.android.util.StringUtils;
import com.skytech.moa.R;
import com.skytech.moa.model.ModuleUtility;
import com.skytech.moa.services.DetailService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.IssueDetailView;
import org.json.JSONArray;
import org.json.JSONObject;

public class FormDetail extends ArkActivity implements IssueDetailView.ClickHappened {
    private IDraftManager draftManager;
    private DetailService model;
    private IssueDetailView view;
    //  private SelectUserView selectUserView;

    private String pkId;
    private String moduleCode, moduleName;
    private String buttonAction;
    private String draftId;
    private int resId;
    private boolean isRefresh = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.START_LOADING:
                    CustomProgress.showProgress(FormDetail.this);
                    break;
                case Constant.DETAIL_SUCCESS:                    //详情加载成功
                    view.refresh((JSONObject) msg.obj, draftManager.getDraft(draftId));
                    break;
                case Constant.DETAIL_FAILURE:               //详情加载失败
                    view.showError(model.getError());
                    break;
                case Constant.BUTTON_OK:
                    view.initButtons((JSONArray) msg.obj);
                    break;
                case Constant.BUTTON_FAILURE:
                    //    view.showMsg(R.string.button_failure);
                    break;
                case Constant.NEXT_STEP_OK:
                    view.showMsg("操作成功");
                    isRefresh = true;
                    if (null != draftId && !draftId.isEmpty()) {
                        draftManager.delete(draftId);
                    }
                    if (buttonAction.equals(Constant.BUTTON_ACTION_SAVE)) {
                        view.setEnabled(true);
                    } else {
                        finish();
                    }
                    break;
                case Constant.NEXT_USER_OK:
                    //    selectUserView.show("", true, model.getUsers());
                    break;
                case Constant.NEXT_STEP_FAILURE:
                case Constant.NEXT_USER_FAILURE:
                    view.showMsg((String) msg.obj);
                    view.setEnabled(true);
                    break;
                case Constant.DWONLOAD_FILE_FAILURE:
                    CustomProgress.hideProgress();
                    view.showMsg("下载文件失败");
                    break;
                case Constant.DWONLOAD_FILE_OK:
                    openFile();
                    CustomProgress.hideProgress();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static Intent createIntent(Context context, String moduleCode, String moduleName, String pkId, String draftId) {
        return createIntent(context, moduleCode, moduleName, pkId, draftId, -1);
    }

    public static Intent createIntent(Context context, String moduleCode, String moduleName, String pkId, String draftId, int resId) {
        Intent intent = new Intent(context, FormDetail.class);
        intent.putExtra(Constant.PARAM_PKID, pkId);
        intent.putExtra(Constant.EXTRA_MODULE_CODE, moduleCode);
        intent.putExtra(Constant.EXTRA_MODULE_NAME, moduleName);
        intent.putExtra(Constant.EXTRA_DRAFT, draftId);
        intent.putExtra(Constant.EXTRA_LAYOUT_RESID, resId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        restoreInstanceState(savedInstanceState);
        draftManager = new DraftManager();
        setTitle(moduleName);

        view = new IssueDetailView(this, this);
        view.setContentView(resId);
        //  selectUserView = new SelectUserView(this, this);
        model = new DetailService(handler);
        try {
            model.setModuleInfo(ModuleUtility.getInstance().getModuleInfo(moduleCode));
        } catch (Exception e) {
            CustomToast.show(this, "module code is undefinition");
            finish();
        }
        onClickRefresh();
    }

    @Override
    public void onClickRefresh() {
        if (!StringUtils.isEmpty(pkId) && moduleCode != null) {
            model.load(pkId);
        }
    }

    @Override
    public void onClickSaveDraft(JSONObject data) {
        draftId = draftManager.saveDraft(draftId, moduleCode, pkId, data.toString());
        view.showMsg("保存草稿成功！");
    }

  /*  @Override
    public void onClickOk(String userId) {
        //  model.sendNextStep(moduleCode, buttonAction, pkId, fields, opinion, userId, mainbodyFileName);
    }

    @Override
    public void onClickCancel() {
        view.setEnabled(true);
    }*/

    @Override
    public void onClickFile(String fileId, String conId, String fileName, boolean isMainBody) {
        CustomProgress.showProgress(this);
        model.downloadsFile(fileId, conId, fileName);
    }

    @Override
    public void onClickWorkStepMonitor() {

    }

    @Override
    public void onClickSendNextStep(String action, JSONObject fields) {
        buttonAction = action;
        model.sendNextStep(moduleCode, action, pkId, fields);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constant.EXTRA_MODULE_CODE, moduleCode);
        outState.putString(Constant.PARAM_PKID, pkId);
        outState.putString(Constant.EXTRA_DRAFT, draftId);
        outState.putInt(Constant.EXTRA_LAYOUT_RESID, resId);
        super.onSaveInstanceState(outState);
    }

    private void restoreInstanceState(Bundle savedState) {
        if (savedState == null) {
            Intent intent = getIntent();
            moduleCode = intent.getStringExtra(Constant.EXTRA_MODULE_CODE);
            moduleName = intent.getStringExtra(Constant.EXTRA_MODULE_NAME);
            pkId = intent.getStringExtra(Constant.PARAM_PKID);
            draftId = intent.getStringExtra(Constant.EXTRA_DRAFT);
            resId = intent.getIntExtra(Constant.EXTRA_LAYOUT_RESID, -1);
        } else {
            moduleCode = savedState.getString(Constant.EXTRA_MODULE_CODE);
            moduleName = savedState.getString(Constant.EXTRA_MODULE_NAME);
            pkId = savedState.getString(Constant.PARAM_PKID);
            draftId = savedState.getString(Constant.EXTRA_DRAFT);
            resId = savedState.getInt(Constant.EXTRA_LAYOUT_RESID, -1);
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, new Intent().putExtra("isRefresh", isRefresh));
        super.finish();
    }

    /**
     * 打开附件
     */
    private void openFile() {
        if (model.getFile() == null || !model.getFile().exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = FileUtils.getMIMEType(model.getFile());
        intent.setDataAndType(Uri.fromFile(model.getFile()), type);
        ResolveInfo ri = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (ri == null) {
            //没有打开的应用
            CustomToast.show(this, "囧，该文件无法打开");
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        view.onActivityResult(requestCode, resultCode, data);
    }
}
