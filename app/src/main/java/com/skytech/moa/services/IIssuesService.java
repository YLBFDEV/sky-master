package com.skytech.moa.services;

import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.UrlCache;

public interface IIssuesService {
    /**
     * to load issues
     *
     * @param url
     * @param callback
     */
    public void load(UrlCache url, ArkHttpClient.HttpHandler callback, RequestParams params);
}
