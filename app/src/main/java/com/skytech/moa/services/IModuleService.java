package com.skytech.moa.services;


import com.skytech.moa.model.Module;

import java.util.List;
import java.util.Map;

public interface IModuleService {
    public final static String ALL_MODULES = "com.skytech.moa.modules.all";

    /**
     * module load handler definition
     */
    public interface LoadModuleHandler {
        /**
         * load modules successfully
         * @param modules modules returned
         */
        public abstract void onSuccess(List<Module> modules);

        /**
         * failed to load modules
         * @param errorMsg error message
         */
        public abstract void onFailure(String errorMsg);
    }

    /**
     * update reminds handler definition
     */
    public interface UpdateRemindsHandler {
        /**
         * update reminds successfully
         * @param remindsMap
         */
        public abstract void onSuccess(Map<String, Integer> remindsMap);

        /**
         * failed to update reminds
         * @param errorMsg
         */
        public abstract void onFailure(String errorMsg);
    }

    /**
     * to load modules by module code given, set ALL_MODULES if you want to load all modules
     * @param moduleCode
     * @param handler callback
     * @throws IllegalArgumentException you should handle IllegalArgumentException
     */
    public void load(String moduleCode, LoadModuleHandler handler) throws IllegalArgumentException;

    /**
     * to update reminds
     * @param handler callback
     */
    public void updateReminds(UpdateRemindsHandler handler);

    /**
     * to get remind count
     * @param moduleCode
     * @return
     */
    public int getRemindCount(String moduleCode);
}
