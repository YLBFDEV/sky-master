package com.skytech.moa.services;

import android.os.Handler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.util.StringUtils;
import com.skytech.android.util.cache.SkyCache;
import com.skytech.moa.App;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralListManager extends BaseService {
    private String url;

    public GeneralListManager(Handler handler) {
        super(handler);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void refresh(JSONObject params) {
        pageNum = 1;
        params = addParams(params);
        get(url, params);
    }

    public void load(JSONObject params) {
        if (StringUtils.isEmpty(url)) return;
        readCache(url);
        refresh(params);
    }

    public void more(JSONObject params) {
        params = addParams(params);
        pageNum++;
        get(url, params);
    }

    private JSONObject addParams(JSONObject params) {
        try {
            params.put(Constant.PARAM_USERID, App.getInstance().getUser().getId());
            params.put(Constant.PARAM_PAGENUM, "" + pageNum);
            params.put(Constant.PARAM_PAGESIZE, PAGE_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    protected void get(final String url, final JSONObject params) {
        if (StringUtils.isEmpty(url)) return;
        sendMsg(Constant.START_LOADING);
        httpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.LIST_SUCCESS, response);
                    if (response.optInt(Constant.PARAM_PAGENUM, 1) == 1) {
                        SkyCache.getInstance().write(url, response.toString());
                    }
                } else {
                    sendMsg(Constant.LIST_FAILURE, response.optString(Constant.PARAM_MESSAGE));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.LIST_FAILURE, "网络不给力");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                sendMsg(Constant.LIST_FAILURE, "网络不给力");
            }
        });
    }

    public void readCache(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(Constant.START_CACHE, SkyCache.getInstance().getJson(url));
            }
        }).start();
    }
}
