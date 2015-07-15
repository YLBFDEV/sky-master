package com.ntko.app.office.documents;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 *
 * Created by xjiaoyang on 2015/1/22.
 */
public class MobileOfficeActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DocumentsAgent.connect(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DocumentsAgent.connect(this);
    }

    @Override
    protected void onDestroy() {
        DocumentsAgent.destroy(this);
        super.onDestroy();
    }

    /**
     * 编辑Office文档
     *
     * @param params 参数
     */
    @Deprecated
    public static void editOfficeDocument(Params params) {
        DocumentsAgent.editWord(params);
    }

    /**
     * 编辑PDF文档
     *
     * @param params 参数
     */
    public static void editPDFDocument(Params params) {
        DocumentsAgent.editPDFDocument(params);
    }

    /**
     * 自定义表单域
     *
     * @param formFields 表单域
     */
    public static void setFormFields(CustomFields formFields) {
        DocumentsAgent.setFormFields(formFields);
    }

}
