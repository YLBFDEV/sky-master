package com.skytech.moa.services;

import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.android.http.UrlCache;

public class AffairService implements IAffairService{
    @Override
    public void applyAffair(UrlCache url, ArkHttpClient.HttpHandler callback, RequestParams params) {
        new HttpCache().get(url, params, callback);
    }
}
