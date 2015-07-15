package com.ntko.app.office.documents;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.ntko.app.office.documents.support.OfficeSupportCompatV1;

public class MobileOfficeFragmentActivity extends FragmentActivity {
    static {
        //设定移动编辑版本: PDF独立版本，标准版本，高级版本
        DocumentsAgent.setOfficeCompatCode(OfficeSupportCompatV1.OfficeCompatCode.OFFICE_PRO);
        //确保每次文件都要上传到服务器
        //即便是只读打开
        Params.DO_NOT_UPLOAD_IF_READONLY = true;
    }

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
        MobileOfficeConnector.connect(this);
    }

    @Override
    protected void onDestroy() {
        DocumentsAgent.destroy(this);
        super.onDestroy();
    }
}
