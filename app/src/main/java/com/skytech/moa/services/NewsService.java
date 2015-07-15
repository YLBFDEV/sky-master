package com.skytech.moa.services;

import android.os.Handler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.util.cache.SkyCache;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.model.NewsInfo;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsService extends BaseService {

    private static String url = API.NEWS;

    public NewsService(Handler handler) {
        super(handler);
    }


    public void refresh(JSONObject params) {
        pageNum = 1;
        params = addParams(params);
        get(params);
    }

    public void load(JSONObject params) {
        readCache(url);
        refresh(params);
    }

    public void more(JSONObject params) {
        params = addParams(params);
        pageNum++;
        get(params);
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

    public void get(JSONObject params) {
        sendMsg(Constant.START_LOADING);
        httpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.LIST_FAILURE,"网络不给力");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                sendMsg(Constant.LIST_FAILURE,"网络不给力");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.LIST_SUCCESS, array2List(response), pageNum);
                    if (response.optInt(Constant.PARAM_PAGENUM, 1) == 1) {
                        SkyCache.getInstance().write(url, response.toString());
                    }
                } else {
                    sendMsg(Constant.LIST_FAILURE, response.optString(Constant.PARAM_MESSAGE));
                }
            }
        });
    }

    public void readCache(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(Constant.START_CACHE, array2List(SkyCache.getInstance().getJson(url)));
            }
        }).start();
    }

    private ArrayList<NewsInfo> array2List(JSONObject json) {
        ArrayList<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
        if (null != json) {
            JSONArray jsonArray = json.optJSONArray("list");
            JSONObject jsonObject = null;
            NewsInfo newsInfo;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.optJSONObject(i);
                newsInfo = new NewsInfo();
                newsInfo.setId(jsonObject.optString("id"));
                newsInfo.setTitle(jsonObject.optString("title"));
                newsInfo.setTime(jsonObject.optString("time"));
                newsInfo.setPicUrl(jsonObject.optString("picUrl"));
                newsInfo.setPraiseNum(jsonObject.optInt("praiseNum"));
                newsInfo.setIsPraise(jsonObject.optBoolean("isRead"));
                newsInfo.setWebUrl(jsonObject.optString("webUrl"));
                newsInfos.add(newsInfo);
            }
        }
        return newsInfos;
    }
}
