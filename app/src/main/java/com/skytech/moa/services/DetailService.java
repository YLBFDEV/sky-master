package com.skytech.moa.services;

import android.os.Handler;
import android.util.Log;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.common.StringUtils;
import com.skytech.android.util.PathUtil;
import com.skytech.android.util.log.ArkLogger;
import com.skytech.android.util.log.LoggerConfig;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.model.ModuleInfo;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 表单详情服务器交互类
 */
public class DetailService extends BaseService {
    private final String TAG = "DetailService";
    private File file;
    private String loadUrl, buttonsUrl, submitUrl;

    public DetailService(Handler handler) {
        super(handler);
    }

    public void setModuleInfo(ModuleInfo moduleInfo) throws Exception {
        if (null == moduleInfo) throw new Exception("url is null");
        this.loadUrl = moduleInfo.getDetailUrl();
        this.buttonsUrl = moduleInfo.getButtonsUrl();
        this.submitUrl = moduleInfo.getSubmitUrl();
    }

    public void load(String pkId) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constant.PARAM_USERID, App.getInstance().getUser().getId());
            params.put(Constant.PARAM_PKID, pkId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadDetailForm(params);
        loadButtons(params);
    }

    /**
     * if pkId='',then get empty form from server by http get method
     * like as disputeInfo/''
     * <p/>
     * if pkId=58fac31d-012a-4438-993e,then get detail info from server by http get method
     * like as disputeInfo/58fac31d-012a-4438-993e
     *
     * @param params
     */
    public void loadDetailForm(JSONObject params) {
        if (null == loadUrl) {
            ArkLogger.e(LoggerConfig.LOG_TAG, "form load url is null");
            return;
        }
        sendMsg(Constant.START_LOADING);
        httpClient.get(loadUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.DETAIL_SUCCESS, response);
                } else {
                    sendMsg(Constant.DETAIL_FAILURE, response.optString(Constant.PARAM_MESSAGE));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.DETAIL_FAILURE, "网络不给力");
                ArkLogger.e(LoggerConfig.LOG_TAG, "网络不给力", throwable);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                sendMsg(Constant.DETAIL_FAILURE, "网络不给力");
                ArkLogger.e(LoggerConfig.LOG_TAG, "网络不给力", throwable);
            }
        });
    }

    public void loadButtons(JSONObject params) {
        if (StringUtils.isEmpty(buttonsUrl)) {
            return;
        }
        httpClient.get(buttonsUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.BUTTON_OK, response.optJSONArray(Constant.PARAM_BUTTONS));
                } else {
                    sendMsg(Constant.BUTTON_FAILURE, response.optString(Constant.PARAM_MESSAGE));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.BUTTON_FAILURE, "网络不给力");
                ArkLogger.e(LoggerConfig.LOG_TAG, "网络不给力", throwable);
            }
        });
    }

    public void loadNextOpusr(String procid) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constant.PARAM_USERID, App.getInstance().getUser().getId());
            params.put(Constant.PARAM_PKID, procid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      /*  httpClient.post(API.NEXT_OPUSR, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(Logging.LOG_TAG, response.toString());
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    users = response.optJSONArray(Constant.PARAM_GROUPS);
                    sendMsg(Constant.NEXT_USER_OK);
                } else {
                    sendMsg(Constant.NEXT_USER_FAILURE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.NEXT_USER_FAILURE);
                Log.e(TAG, responseString);
            }
        });*/
    }

    public void sendNextStep(String moduleCode, String action, String pkId, JSONObject data) {
        switch (action) {
            case Constant.BUTTON_ACTION_SAVE:
                submit(data);
                break;
        }
    }

    private void submit(JSONObject data) {
        httpClient.post(submitUrl, data, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.NEXT_STEP_OK);
                } else {
                    sendMsg(Constant.NEXT_STEP_FAILURE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.NEXT_STEP_FAILURE);
                Log.e(TAG, error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                sendMsg(Constant.NEXT_STEP_FAILURE);
                Log.e(TAG, error);
            }
        });
    }

    private void prepareData(String action, String pkId, JSONObject data) {
        try {
            data.put("create_id", App.getInstance().getUser().getId());
            data.put("pkid", pkId);
            data.put("action", action);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void downloadsFile(String fileId, String conid, String fileName) {
        if (null != fileId) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", fileId);
                json.put(Constant.PARAM_CONID, conid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            File dwfile = new File(PathUtil.getInstance().getAttachmentDir() + File.separator + fileName);
            httpClient.post(API.GET_FILE, json, new FileAsyncHttpResponseHandler(dwfile) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    sendMsg(Constant.DWONLOAD_FILE_FAILURE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File f) {
                    sendMsg(Constant.DWONLOAD_FILE_OK);
                    file = f;
                }
            });
        }
    }

    public File getFile() {
        return file;
    }
}
