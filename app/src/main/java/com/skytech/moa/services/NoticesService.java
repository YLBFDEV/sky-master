package com.skytech.moa.services;

import android.os.Handler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.util.cache.SkyCache;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.model.NoticeInfo;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticesService extends BaseService {

    private static final String TAG = NoticesService.class.getSimpleName();

    private static String url = API.NOTICE;

    public NoticesService(Handler handler) {
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
                sendMsg(Constant.LIST_FAILURE);
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

    private ArrayList<NoticeInfo> array2List(JSONObject json) {
        ArrayList<NoticeInfo> noticeInfos = new ArrayList<NoticeInfo>();
        if (null != json) {
            JSONArray jsonArray = json.optJSONArray("list");
            JSONObject jsonObject = null;
            NoticeInfo noticeInfo;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.optJSONObject(i);
                noticeInfo = new NoticeInfo();
                noticeInfo.setId(jsonObject.optString("nid"));
                noticeInfo.setTitle(jsonObject.optString("title"));
                noticeInfo.setDept(jsonObject.optString("dept"));
                noticeInfo.setReleaseTime(jsonObject.optString("releaseTime"));
                noticeInfo.setUrl(jsonObject.optString("url"));
                noticeInfo.setHasAttachment(jsonObject.optBoolean("hasAttachment"));
                noticeInfos.add(noticeInfo);
            }
        }
        return noticeInfos;
    }

}
