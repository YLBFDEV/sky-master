package com.skytech.moa.view.home;



import com.skytech.moa.model.Module;

import java.util.List;
import java.util.Map;

public interface IModuleLoaderView {

    public void load(List<Module> modules);

    public void setRemindModules(Map<String, Integer> remindsMap);

    public void loadModuleFailed();
}
