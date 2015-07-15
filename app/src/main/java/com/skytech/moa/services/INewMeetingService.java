package com.skytech.moa.services;

import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.UrlCache;

public interface INewMeetingService {

    public void newMeeting(UrlCache url, ArkHttpClient.HttpHandler callback, RequestParams params);
}
