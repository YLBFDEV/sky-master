package com.skytech.moa.model;

import com.skytech.moa.API;
import com.skytech.moa.R;
import com.skytech.moa.activity.*;
import com.skytech.moa.utils.Constant;

import java.util.HashMap;
import java.util.Map;

public class ModuleUtility {
    private static ModuleUtility instance;
    private Map<String, ModuleInfo> moduleMap = new HashMap<>();

    public int getModuleImage(String key) {
        return moduleMap.get(key).getImage();
    }

    public String getListUrl(String key) throws Exception {
        ModuleInfo m = moduleMap.get(key);
        if (null == m) throw new Exception("module key is undefinition ");
        return moduleMap.get(key).getListUrl();
    }

    public Class<?> getModuleActivity(String key) {
        return moduleMap.get(key).getCls();
    }

    public ModuleInfo getModuleInfo(String key) {
        return moduleMap.get(key);
    }

    private void initModule() {
        moduleMap.clear();
        moduleMap.put(Constant.MODULE_DBSX, new ModuleInfo(R.drawable.ic_dbsx, NewsActivity.class));
        moduleMap.put(Constant.MODULE_NEWS, new ModuleInfo(R.drawable.ic_dbsx, GeneralList.class));
        moduleMap.put(Constant.MODULE_TZGG, new ModuleInfo(R.drawable.ic_tzgg, NoticeActivity.class));
        moduleMap.put(Constant.MODULE_RCAP, new ModuleInfo(R.drawable.ic_rcap, null));
        moduleMap.put(Constant.MODULE_GZYJ, new ModuleInfo(R.drawable.ic_gzyj, null));
        moduleMap.put(Constant.MODULE_TXL, new ModuleInfo(R.drawable.ic_txl, Contacts.class));
        moduleMap.put(Constant.MODULE_RCSW, new ModuleInfo(R.drawable.ic_rcsw, GeneralList.class));
        moduleMap.put(Constant.MODULE_RCSW_SWSQ, new ModuleInfo(R.drawable.ic_rcsw, GeneralList.class, API.AFFAIR_LIST, API.AFFAIR_FORM, API.AFFAIR_FORM_BUTTONS, API.AFFAIR_FORM_SAVE));
        moduleMap.put(Constant.MODULE_KHGL, new ModuleInfo(R.drawable.ic_khgl, Contacts.class));
        moduleMap.put(Constant.MODULE_WDK, new ModuleInfo(R.drawable.ic_wdk, DocLibrary.class));
        moduleMap.put(Constant.MODULE_CLDT, new ModuleInfo(R.drawable.ic_cldt, null));
        moduleMap.put(Constant.MODULE_CLDT, new ModuleInfo(R.drawable.ic_cldt, null));
        moduleMap.put(Constant.MODULE_EXPENSE, new ModuleInfo(API.EXPENSE_LIST, API.EXPENSE_FORM, API.EXPENSE_FORM_BUTTONS, API.EXPENSE_FORM_SAVE));
        moduleMap.put(Constant.MODULE_NEWWORKLOG, new ModuleInfo("", API.NEWWORKLOGS_FORM, API.NEWWORKLOGS_FORM_BUTTONS, API.NEWWORKLOGS_FORM_SAVE));
        moduleMap.put(Constant.MODULE_NEWMEETING, new ModuleInfo(API.MEETING_LIST, API.NEWMEETING_FORM, API.NEWMEETING_FORM_BUTTONS, API.NEWMEETING_FORM_SAVE));
        moduleMap.put(Constant.MODULE_NEWWORKPLAN, new ModuleInfo("", API.NEWWORKPLAN_FORM, API.NEWWORKPLAN_FORM_BUTTONS, API.NEWWORKPLAN_FORM_SAVE));
        moduleMap.put(Constant.MODULE_NEWMEMO, new ModuleInfo("", API.NEWMEMO_FORM, API.NEWMEMO_FORM_BUTTONS, API.NEWMEMO_FORM_SAVE));
        moduleMap.put(Constant.MODULE_KEY_ATTENDANCE_EXCEPTION, new ModuleInfo(API.ATTENDANCE_EXCEPTION_LIST, API.ATTENDANCE_EXCEPTION_DETAIL, null, API.ATTENDANCE_EXCEPTION_SAVE));
        moduleMap.put(Constant.MODULE_TJGN, new ModuleInfo(R.drawable.ic_tjgn, null));
    }

    private ModuleUtility() {
        initModule();
    }

    public static synchronized ModuleUtility getInstance() {
        if (null == instance) {
            instance = new ModuleUtility();
        }
        return instance;
    }
}