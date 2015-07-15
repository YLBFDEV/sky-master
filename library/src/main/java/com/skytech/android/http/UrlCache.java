package com.skytech.android.http;

import com.skytech.android.cache.CacheType;
import com.skytech.android.util.log.ArkLogger;
import com.skytech.android.util.log.LoggerConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UrlCache implements Serializable {

    private String url;
    private CacheType cacheType;
    private HttpMethods httpMethods;

    public UrlCache(JSONObject json) {
        url = json.optString("url").toLowerCase();
        if (json.optString("cache_type").equalsIgnoreCase("1")) {
            cacheType = CacheType.LONG;
        } else if (json.optString("cache_type").equalsIgnoreCase("2")) {
            cacheType = CacheType.SHORT;
        } else {
            cacheType = CacheType.NOCACHE;
        }
        if (json.optString("type").equalsIgnoreCase("post")) {
            httpMethods = HttpMethods.POST;
        } else if (json.optString("type").equalsIgnoreCase("put")) {
            httpMethods = HttpMethods.PUT;
        } else if (json.optString("type").equalsIgnoreCase("delete")) {
            httpMethods = HttpMethods.DELETE;
        } else {
            httpMethods = HttpMethods.GET;
        }
    }

    public UrlCache(String url) {
        this(url, CacheType.NOCACHE);
    }

    public UrlCache(String url, CacheType cacheType) {
        this.url = url;
        this.cacheType = cacheType;
    }

    public UrlCache(String url, CacheType cacheType, HttpMethods httpMethods) {
        this.url = url;
        this.cacheType = cacheType;
        this.httpMethods = httpMethods;
    }

    public String getUrl() {
        return url;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public HttpMethods getHttpMethods() {
        return httpMethods;
    }

    public void setHttpMethods(HttpMethods httpMethods) {
        this.httpMethods = httpMethods;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("url", url);
            json.put("cache_type", cacheType());
            json.put("type", type());
        } catch (JSONException e) {
            ArkLogger.e(LoggerConfig.LOG_TAG, e.getMessage());
        }
        return json;
    }

    private String cacheType() {
        if (cacheType == CacheType.LONG)
            return "1";
        else if (cacheType == CacheType.SHORT)
            return "2";
        else return "3";
    }

    private String type() {
        if (httpMethods == HttpMethods.POST)
            return "post";
        else if (httpMethods == HttpMethods.GET)
            return "get";
        else if (httpMethods == HttpMethods.PUT)
            return "put";
        else return "delete";
    }
}
