package com.skytech.moa.doc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.skytech.android.Logging;
import com.skytech.android.util.CustomToast;
import com.skytech.android.util.JsonUtil;
import com.skytech.moa.doc.widgets.*;
import com.skytech.moa.doc.widgets.attachment.FieldAttachment;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FormView extends LinearLayout {
    public interface Callback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    private Callback callback;
    private FieldAttachment fieldAttachment;
    private List<BaseFieldView> fieldViews;
    private Map<String, FieldOpinionText> opinionViews;
    private int padding;
    private JSONObject draft;
    private boolean isEditable = false;
    private boolean isAddable = false;

    public FormView(Context context) {
        this(context, null, -1);
    }

    public FormView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.doc);
    }

    public FormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
        padding = (int) getResources().getDimension(R.dimen.main_content_margin);
    }

    public void setContentView(int resId) {
        LayoutInflater.from(getContext()).inflate(resId, this);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setUploadHandler(final FieldAttachment.UploadHandler uploadHandler) {
        fieldAttachment.setUploadHandler(uploadHandler);
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setAddable(boolean isAddable) {
        this.isAddable = isAddable;
    }

    public boolean need2SaveSignature() {
        for (String key : opinionViews.keySet()) {
            if (opinionViews.get(key).need2Save())
                return true;
        }
        return false;
    }

    private void addField(JSONObject json) {
        if (null == json) return;
        BaseFieldView fieldView;
        switch (json.optString(Constant.FIELD_TYPE)) {
            case Constant.FIELD_TEXT:
            case Constant.FIELD_INPUT:
                fieldView = new FieldEditText(getContext());
                break;
            case Constant.FIELD_OPINION:
                fieldView = new FieldChoiceView(getContext());
                break;
            case Constant.FIELD_DATE:
                fieldView = new FieldDateText(getContext());
                break;
            case Constant.SUB_FORM:
                fieldView = new SubFormView(getContext());
                break;
            case Constant.FILE:
                fieldView = new FieldAttachment(getContext());
                break;
            default:
                fieldView = new FieldEditText(getContext());
                break;
        }
        fieldView.setPadding(padding, padding, padding, padding);
        fieldView.setKeyId(json.optString(Constant.FIELD_KEYID));
        fieldView.setKeyName(json.optString(Constant.FIELD_KEYNAME));
        if (null != draft && draft.has(fieldView.getKeyId())) {
            fieldView.setValue(draft.opt(fieldView.getKeyId()));
        } else {
            fieldView.setValue(json.opt(Constant.FIELD_VALUE));
        }
        fieldView.setEditable(isEditable ? json.optBoolean(Constant.FIELD_EDITABLE, false) : isEditable);
        fieldView.setRequired(json.optBoolean(Constant.FIELD_REQUIRED, false));

        //set special attribute
        switch (json.optString(Constant.FIELD_TYPE)) {
            case Constant.FIELD_TEXT:
            case Constant.FIELD_INPUT:
                ((FieldEditText) fieldView).setInputType(json.optString("inputtype"));
                ((FieldEditText) fieldView).setMaxLength(json.optInt("maxlength", 20));
                break;
            case Constant.FIELD_OPINION:
                ((FieldChoiceView) fieldView).setIsMultiple(json.optBoolean("ismultiple", false));
                ((FieldChoiceView) fieldView).setOptions(json.optJSONArray("options"));
                ((FieldChoiceView) fieldView).setValueId(json.optString("valueid"));

                if (null != draft && draft.has(fieldView.getKeyId())) {
                    ((FieldChoiceView) fieldView).setValueId(draft.opt(fieldView.getKeyId()));
                }
                break;
            case Constant.SUB_FORM:
                ((SubFormView) fieldView).setFormViewFields(json.optJSONArray(Constant.DOC_FIELDS));
                ((SubFormView) fieldView).setFormViewsData(json.optJSONArray("list"));

                if (null != draft && draft.has(fieldView.getKeyId())) {
                    ((SubFormView) fieldView).setFormViewsData(draft.opt(fieldView.getKeyId()));
                } else {
                    ((SubFormView) fieldView).setAddable(isAddable);
                }
                break;
            case Constant.FILE:
                fieldAttachment = (FieldAttachment) fieldView;
                setCallback(fieldAttachment);
                if (null != draft && draft.has(fieldView.getKeyId())) {
                    fieldAttachment.setValue(draft.opt(Constant.DOC_ATTACHMENTS));
                }
                break;
        }

        addView(fieldView);
    }

    private void initFields(JSONArray array) {
        if (null == array || array.length() == 0) return;
        for (int i = 0; i < array.length(); i++) {
            addField(array.optJSONObject(i));
        }
    }

    private void addAttachment(JSONObject json) {
        if (null == json) return;
        FieldAttachment fieldAttachment = new FieldAttachment(getContext());
        setCallback(fieldAttachment);
        fieldAttachment.setValue(json);
    }

    public void initAttachment(JSONArray array) {
        if (null == array || array.length() == 0) return;
        for (int i = 0; i < array.length(); i++) {
            addAttachment(array.optJSONObject(i));
        }
    }

    public void initFieldViews(JSONArray fields, JSONObject draft) {
        this.draft = draft;
        removeAllViews();
        initFields(fields);
        opinionViews = new HashMap<String, FieldOpinionText>();
        fieldViews = new LinkedList<BaseFieldView>();
        traverseFieldText(this);
    }

    public void setData(JSONObject data) {
        if (null == data) return;
        for (BaseFieldView field : fieldViews) {
            initFieldView(field, data.optJSONObject(field.getKeyId()));
        }
    }

    public void setFieldViewsData(JSONObject data) {
        if (null == data) return;
        for (BaseFieldView field : fieldViews) {
            field.setValue(data.optString(field.getKeyId()));
            if (field instanceof FieldChoiceView) {
                field.setValueId(data.optString(field.getKeyId()));
            }
        }
    }

    private void setText(int viewId, String text) {
        if (null == text || null == findViewById(viewId)) return;
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void setData(JSONArray array) {
        if (null == array) return;
        JSONObject fieldsJson = new JSONObject();
        for (int i = 0; i < array.length(); i++) {
            try {
                fieldsJson.put(array.optJSONObject(i).optString("keyid"), array.optJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (BaseFieldView field : fieldViews) {
            initFieldView(field, fieldsJson.optJSONObject(field.getKeyId()));
        }
    }

    public void placeSign(String position, String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
        placeSign(position, bitmap);
    }

    public void placeSign(String position, Bitmap signature) {
        Log.d(Logging.LOG_TAG, "try to place signature at " + position);
        for (String key : opinionViews.keySet()) {
            opinionViews.get(key).addSignImage(position, signature);
        }
    }

    private void initFieldView(BaseFieldView fieldView, JSONObject fieldJson) {
        if (fieldJson != null && null != fieldView) {
            fieldView.setValueId(fieldJson.optString("valueid"));
            fieldView.setEditable(fieldJson.optBoolean("editable", false));
            fieldView.setKeyName(fieldJson.optString("keyname"));
            fieldView.setRequired(fieldJson.optBoolean("required", false));
            if (fieldView instanceof FieldOpinionText) {
                fieldView.setValue(fieldJson.optJSONArray("values"));
            } else {
                fieldView.setValue(fieldJson.optString("value"));
            }
            if (fieldView instanceof FieldChoiceView) {
                ((FieldChoiceView) fieldView).setOptions(fieldJson.optJSONArray("options"));
            }
        }
    }

    public boolean validate() {
        String msg = "";
        for (BaseFieldView field : fieldViews) {
            if (field.isNeed2Validate()) {
                if (msg.length() == 0) {
                    msg += "请输入";
                } else {
                    msg += "，";
                }
                msg += String.format("【%s】", field.requiredName());
            }
        }

        if (msg.length() == 0) {
            return true;
        } else {
            CustomToast.show(getContext(), msg);
            return false;
        }
    }

    public JSONObject getFieldsData() {
        JSONObject json = new JSONObject();
        for (BaseFieldView field : fieldViews) {
            if ((field instanceof FieldOpinionText) == false) {
                if (null == field.getValueId()) {
                    Log.e("FormView", "field value is null");
                } else {
                    try {
                        json.put(field.getKeyId(), field.getValueId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return json;
    }

    public JSONArray getFieldAttachmentData() {
        if (fieldAttachment != null) {
            return JsonUtil.list2JSONArray(fieldAttachment.getPaths());
        } else {
            return new JSONArray();
        }
    }

    public JSONArray getOpinions() {
        JSONArray array = new JSONArray();
        for (BaseFieldView field : fieldViews) {
            if (field instanceof FieldOpinionText) {
                if (field.isEditable()) {
                    JSONObject fieldJson = new JSONObject();
                    try {
                        fieldJson.put(field.getKeyId(), field.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(fieldJson);
                }
            }
        }
        return array;
    }

    public String getOpinion() {
        for (BaseFieldView field : fieldViews) {
            if (field instanceof FieldOpinionText) {
                if (field.isEditable()) {
                    return (String) field.getValue();
                }
            }
        }
        return "";
    }

    /**
     * 遍历布局中的 @{FieldText}
     */
    private void traverseFieldText(ViewGroup group) {
        int viewCount = group.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View v = group.getChildAt(i);
            if (v instanceof BaseFieldView) {
                fieldViews.add((BaseFieldView) v);
                if (v instanceof FieldOpinionText) {
                    final FieldOpinionText fot = (FieldOpinionText) v;
                    opinionViews.put(fot.getKeyId(), fot);
                }
            } else if (v instanceof ViewGroup) {
                ViewGroup g = (ViewGroup) v;
                if (g.getChildCount() != 0) {
                    traverseFieldText(g);
                }
            }
        }
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        if (opinionViews == null) return;
        for (String key : opinionViews.keySet()) {
            opinionViews.get(key).setParentScrollView(parentScrollView);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != callback)
            callback.onActivityResult(requestCode, resultCode, data);
    }
}
