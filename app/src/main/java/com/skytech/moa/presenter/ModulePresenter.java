package com.skytech.moa.presenter;

import android.content.Intent;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.App;
import com.skytech.moa.activity.Home;
import com.skytech.moa.model.Module;
import com.skytech.moa.services.IModuleService;
import com.skytech.moa.services.ModuleService;
import com.skytech.moa.view.home.IModuleLoaderView;

import java.util.List;
import java.util.Map;

public class ModulePresenter {
    public final static String ALL_MODULES = "com.skytech.moa.modules.all";

    private IModuleLoaderView moduleLoaderView;
    private IModuleService moduleService;

    private ModulePresenter() {
        moduleService = ModuleService.getInstance();
    }

    public ModulePresenter(IModuleLoaderView moduleLoaderView) {
        this();
        this.moduleLoaderView = moduleLoaderView;
    }

    /**
     * to load module specified
     *
     * @param moduleCode module`s code, set to ALL_MODULES if you want to load all modules
     */
    public void load(String moduleCode) {
        moduleService.load(moduleCode, new IModuleService.LoadModuleHandler() {
            @Override
            public void onSuccess(List<Module> modules) {
                moduleLoaderView.load(modules);
            }

            @Override
            public void onFailure(String errorMsg) {
                moduleLoaderView.loadModuleFailed();
            }
        });
    }

    /**
     * to update reminds
     */
    public void updateReminds() {
        moduleService.updateReminds(new IModuleService.UpdateRemindsHandler() {
            @Override
            public void onSuccess(Map<String, Integer> remindsMap) {
                notifyRemindsUpdated();
                moduleLoaderView.setRemindModules(remindsMap);
            }

            @Override
            public void onFailure(String errorMsg) {
                CustomToast.show(App.getInstance().getContext(), errorMsg);
            }
        });
    }

    /**
     * to get module`s remind count
     *
     * @param moduleCode module code
     * @return remind count
     */
    public int getRemindCount(String moduleCode) {
        return moduleService.getRemindCount(moduleCode);
    }

    /**
     * to notify reminds has been updated
     */
    private void notifyRemindsUpdated() {
        Intent intent = new Intent();
        intent.setAction(Home.ACTION_REMINDS_UPDATED);
        App.getInstance().getContext().sendBroadcast(intent);
    }
}
