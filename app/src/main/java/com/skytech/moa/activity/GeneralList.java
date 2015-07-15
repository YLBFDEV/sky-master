package com.skytech.moa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.skytech.android.ArkActivity;
import com.skytech.android.draft.DraftManager;
import com.skytech.android.draft.IDraftManager;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.adapter.AffairApplyAdapter;
import com.skytech.moa.adapter.AffairModuleAdapter;
import com.skytech.moa.adapter.AttendanceExceptionAdapter;
import com.skytech.moa.adapter.MeettingManageAdapter;
import com.skytech.moa.model.ModuleUtility;
import com.skytech.moa.services.GeneralListManager;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.GeneralListView;
import org.json.JSONObject;

public class GeneralList extends ArkActivity implements GeneralListView.OnClickHappened {
    public static Intent createIntent(Context context, String moduleCode, String moduleName) {
        Intent i = new Intent(context, GeneralList.class);
        i.putExtra(Constant.EXTRA_MODULE_CODE, moduleCode);
        i.putExtra(Constant.EXTRA_MODULE_NAME, moduleName);
        return i;
    }

    private IDraftManager draftManager;
    private GeneralListManager listModel;
    private GeneralListView view;

    private String moduleCode, moduleName;
    private boolean isServicer = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.START_LOADING:
                    CustomProgress.showProgress(GeneralList.this, "正在加载……");
                    break;
                case Constant.START_CACHE:                    //缓存
                    if (!isServicer) {
                        view.refreshList((JSONObject) msg.obj);
                    }
                    break;
                case Constant.START_DRAFTS:                    //草稿
                    view.showDrafts(draftManager.list(moduleCode));
                    break;
                case Constant.LIST_SUCCESS:                    //列表加载成功
                    isServicer = true;
                    view.refreshList((JSONObject) msg.obj);
                    break;
                case Constant.LIST_FAILURE:                    //列表加载失败
                    view.showError((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_list);
        readInstanceState(savedInstanceState);
        draftManager = new DraftManager();
        listModel = new GeneralListManager(handler);
        view = new GeneralListView(this, this);
        view.setTitle(moduleName);
        switch (moduleCode) {//1.-------------------------------------------------------------------------------------
            case Constant.MODULE_RCSW:
                view.setAdapter(new AffairModuleAdapter(GeneralList.this));
                break;
            case Constant.MODULE_RCSW_SWSQ:
                view.setAdapter(new AffairApplyAdapter(GeneralList.this));
                view.showButtonAdd();
                break;
            case Constant.MODULE_NEWMEETING:
                view.setAdapter(new MeettingManageAdapter(GeneralList.this));
                view.showButtonAdd();
                break;
            case Constant.MODULE_KEY_ATTENDANCE_EXCEPTION:
                view.setAdapter(new AttendanceExceptionAdapter(GeneralList.this));
                view.showButtonAdd();
                break;
            default:
                CustomToast.show(this, "module code is undefinition");
                finish();
                break;
        }
        try {
            listModel.setUrl(ModuleUtility.getInstance().getListUrl(moduleCode));
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constant.EXTRA_MODULE_CODE, moduleCode);
        outState.putString(Constant.EXTRA_MODULE_NAME, moduleName);
        super.onSaveInstanceState(outState);
    }

    private void readInstanceState(Bundle saveState) {
        if (null == saveState) {
            Intent intent = getIntent();
            moduleCode = intent.getStringExtra(Constant.EXTRA_MODULE_CODE);
            moduleName = intent.getStringExtra(Constant.EXTRA_MODULE_NAME);
        } else {
            moduleCode = saveState.getString(Constant.EXTRA_MODULE_CODE);
            moduleName = saveState.getString(Constant.EXTRA_MODULE_NAME);
        }
    }

    private void refresh() {
        listModel.load(view.getParams());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra(Constant.EXTRA_REFRESH, false)) {
                refresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAdd() {
        switch (moduleCode) {//2.-----------------------------------------------------------------------------------
            case Constant.MODULE_KEY_ATTENDANCE_EXCEPTION:
                startActivityForResult(FormDetail.createIntent(GeneralList.this, moduleCode, "异常登记", "", "", R.layout.form_attendance_exception), Constant.REQUEST_LIST);
                break;
            case Constant.MODULE_RCSW_SWSQ:
            case Constant.MODULE_NEWMEETING:
                startActivityForResult(FormDetail.createIntent(GeneralList.this, moduleCode, moduleName, "''", ""), Constant.REQUEST_LIST);
                break;
            default:
                startActivityForResult(FormDetail.createIntent(GeneralList.this, moduleCode, moduleName, "''", ""), Constant.REQUEST_LIST);
                break;
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onMore() {
        listModel.more(view.getParams());
    }

    @Override
    public void onOpenDetail(JSONObject json) {
        switch (moduleCode) {//3.--------------------------------------------------------------------------------------
            case Constant.MODULE_KEY_ATTENDANCE_EXCEPTION:
                startActivityForResult(
                        FormDetail.createIntent(
                                GeneralList.this,
                                moduleCode, "异常编辑",
                                json.optString(Constant.PARAM_ID),
                                json.optString(Constant.EXTRA_DRAFT),
                                R.layout.form_attendance_exception
                        ),
                        Constant.REQUEST_LIST);
                break;
            case Constant.MODULE_RCSW:
                startActivity(GeneralList.createIntent(GeneralList.this, json.optString(Constant.EXTRA_MODULE_CODE), json.optString(Constant.EXTRA_MODULE_NAME)));
                break;
            case Constant.MODULE_RCSW_SWSQ:
            case Constant.MODULE_NEWMEETING:
                startActivityForResult(FormDetail.createIntent(GeneralList.this, moduleCode, moduleName, json.optString(Constant.PARAM_ID), json.optString(Constant.EXTRA_DRAFT)), Constant.REQUEST_LIST);
                break;
            case Constant.MODULE_JFTJ:
                startActivityForResult(FormDetail.createIntent(GeneralList.this, moduleCode, moduleName, json.optString(Constant.PARAM_PKID), json.optString(Constant.EXTRA_DRAFT)), Constant.REQUEST_LIST);
                break;
        }
    }
}
