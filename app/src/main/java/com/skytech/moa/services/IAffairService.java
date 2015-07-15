package com.skytech.moa.services;

import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.UrlCache;

public interface IAffairService {

    public void applyAffair(UrlCache url, ArkHttpClient.HttpHandler callback, RequestParams params);
}
