package com.skytech.android.http;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.*;
import com.skytech.android.Logging;
import com.skytech.R;
import com.skytech.android.SkyInitializer;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.Iterator;

public class SkyHttpClient extends AsyncHttpClient {
    public static String BASE_URL = "appBaseUrl";
    private static SkyHttpClient httpClient;
    private String appBaseUrl;
    private Context context;

    public static SkyHttpClient getInstance() {
        return httpClient;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public static void initialize(Context context) {
        httpClient = new SkyHttpClient(context);
    }

    public SkyHttpClient(Context context) {
        this.context = context;
        appBaseUrl = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE).
                getString(BASE_URL, SkyInitializer.getInstance().getAppUrl());
        //addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
        setTimeout(30 * 1000);
    }

    private String buildFullUrl(String url) {
        url = url.replace("{", "%7b").replace("}", "%7d").replace("\"", "%22").replace(":", "%3a").replace("[", "%5b").replace("]", "%5d");
        Log.d(Logging.LOG_TAG, "http url:" + appBaseUrl + "/" + url);
        return appBaseUrl + "/" + url;
    }

    public RequestHandle get(String url, JSONObject json, ResponseHandlerInterface responseHandler) {
        Log.d(Logging.LOG_TAG, json.toString());
        RequestParams params = new RequestParams();
        Iterator it = json.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            params.put(key, json.opt(key));
        }

        return get(url, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, ResponseHandlerInterface responseHandler) {
        return get(context, url, null, null, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return get(context, url, null, params, responseHandler);
    }

    @Override
    public RequestHandle get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return get(null, url, null, params, responseHandler);
    }

    @Override
    public RequestHandle get(String url, ResponseHandlerInterface responseHandler) {
        return get(null, url, null, null, responseHandler);
    }

    public RequestHandle post(String url, JSONObject params, ResponseHandlerInterface responseHandler) {
        Log.d(Logging.LOG_TAG, params.toString());
        return post(null, url, Json2Entity(params), null, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
        return super.get(context, buildFullUrl(url), headers, params, responseHandler);
    }

    public RequestHandle post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        return super.post(context, buildFullUrl(url), entity, contentType, responseHandler);
    }

    private HttpEntity Json2Entity(JSONObject params) {
        StringEntity stringEntity = null;
        if (params != null) {
            try {
                stringEntity = new StringEntity(params.toString(), "UTF-8");
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                Log.d(Logging.LOG_TAG, "http params:" + params.toString());
            } catch (Exception e) {
                Log.e(Logging.LOG_TAG, e.getMessage());
            }
        }
        return stringEntity;
    }

    public void addCookie(String name, String value) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        myCookieStore.addCookie(newCookie);
        httpClient.setCookieStore(myCookieStore);
    }
}
