package com.skytech.moa.services;

import android.text.TextUtils;
import android.util.Log;
import com.loopj.android.http.RequestParams;
import com.skytech.android.Logging;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.model.Module;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleService implements IModuleService {
    /**
     * singleton
     */
    private static ModuleService instance;
    /**
     * to indicate whether or not it has been initialized
     */
    private boolean initialed;
    /**
     * http client
     */
    private ArkHttpClient httpClient;
    /**
     * global modules
     */
    private final List<Module> gModules = new ArrayList<Module>();
    /**
     * latest update time
     */
    private String updateTime;
    /**
     * global module map for quick search
     */
    private final Map<String, Module> gModuleMap = new HashMap<String, Module>();
    /**
     * global remind map
     */
    private final Map<String, Integer> gRemindsMap = new HashMap<String, Integer>();

    private ModuleService() {
        initialed = false;
        httpClient = new HttpCache();
    }

    public synchronized static ModuleService getInstance() {
        if (null == instance) {
            instance = new ModuleService();
        }
        return instance;
    }


    /**
     * to load modules by module code given, set ALL_MODULES if you want to load all modules
     *
     * @param moduleCode
     * @param handler    callback
     * @throws IllegalArgumentException you should handle IllegalArgumentException
     */
    @Override
    public void load(final String moduleCode, final LoadModuleHandler handler) throws IllegalArgumentException {
        if ((null == moduleCode) || (TextUtils.isEmpty(moduleCode) || (null == handler))) {
            throw new IllegalArgumentException("Parameter Error while Loading Modules.");
        }

        gModules.clear();
        gModuleMap.clear();

        RequestParams params = new RequestParams();
        params.put("uid", "1");
        httpClient.get(API.GET_ALL_MODULES, params, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    JSONArray modules = response.optJSONArray("modules");
                    if (null == modules) {
                        handler.onFailure("http error : modules not container items ' key");
                        return;
                    }
                    for (int i = 0; i < modules.length(); i++) {
                        Module module = parseModule(modules.optJSONObject(i));
                        gModules.add(module);
                        gModuleMap.put(module.getKey(), module);
                    }

                    addCustomFeature();

                    if (moduleCode.equals(ALL_MODULES)) {
                        handler.onSuccess(gModules);
                        return;
                    }

                    Module subModule = gModuleMap.get(moduleCode);
                    if (null == subModule)
                        throw new IllegalArgumentException("Module Code Error while Loading Modules : " + moduleCode);
                    List<Module> children = subModule.getChildren();
                    if (children.size() == 0)
                        throw new IllegalArgumentException("Module Found, BUT it is not a Parent Module : " + moduleCode);
                    handler.onSuccess(children);

                } else {
                    handler.onFailure(response.optString(Constant.PARAM_MESSAGE));
                }
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                handler.onFailure("http error");
            }
        });
    }

    private void  addCustomFeature() {
        Module module = new Module("tjgn", "添加功能", "0");
        gModules.add(module);
        gModuleMap.put(module.getKey(), module);
    }

    /**
     * to update reminds
     *
     * @param handler callback
     */
    @Override
    public void updateReminds(final UpdateRemindsHandler handler) {
        if (null == handler) {
            throw new IllegalArgumentException("Parameter Error while querying todo");
        }
        JSONObject params = new JSONObject();
        try {
            params.put("uid", App.getInstance().getUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpClient.post(API.GET_TODO_NUM, null, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                Log.d(Logging.LOG_TAG, response.toString());
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    parseReminds(response.optJSONArray("modules"));
                    handler.onSuccess(gRemindsMap);
                } else {
                    handler.onFailure(response.optString(Constant.PARAM_MESSAGE));
                }
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                handler.onFailure("http error");
            }
        });
    }

    /**
     * to get remind count
     *
     * @param moduleCode
     * @return
     */
    @Override
    public int getRemindCount(String moduleCode) {
        return (gRemindsMap.containsKey(moduleCode)) ? gRemindsMap.get(moduleCode) : 0;
    }

    /**
     * to parse each module
     *
     * @param object module`s json
     * @return module
     * @throws IllegalArgumentException
     */
    private Module parseModule(JSONObject object) throws IllegalArgumentException {
        // 1, basic information
        Module module = new Module(object.optString("key"), object.optString("module"), object.optString("num"));
        if (Logging.DEBUG) {
            Log.d(Logging.LOG_TAG, "Module Added : " + module.toString());
        }

        // 2, children
        JSONArray array = object.optJSONArray("children");
        if (null != array) {
            List<Module> children = new ArrayList<Module>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    children.add(parseModule(array.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.w(Logging.LOG_TAG, "Parse Module Children ERROR : " + array.toString());
                }
            }
            module.setChildren(children);
        }
        return module;
    }


    /**
     * to parse reminds
     *
     * @param array reminds array
     */
    private void parseReminds(JSONArray array) {
        if ((null == array) || (0 == array.length())) {
            Log.e(Logging.LOG_TAG, "reminds array is empty.");
            return;
        }
        gRemindsMap.clear();
        int count = array.length();
        for (int i = 0; i < count; i++) {
            JSONObject remind = array.optJSONObject(i);
            if ((null == remind) || (remind.isNull("modulekey")) || (TextUtils.isEmpty(remind.optString("num")))) {
                Log.w(Logging.LOG_TAG, "remind got from server has empty value, you`d better check it");
                continue;
            }
            gRemindsMap.put(remind.optString("modulekey"), remind.optInt("num"));
        }
    }
}
