package com.skytech.moa.services;

import com.skytech.android.cache.CacheType;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.android.http.UrlCache;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/4.
 */
public class NewsPraiseService {

    private ArkHttpClient httpClient;
    private JSONObject jsonObject;

    public static UrlCache PRAISE_NEWS = new UrlCache("news/praiseNews.do", CacheType.NOCACHE);

    public NewsPraiseService() {
        httpClient = new HttpCache();

    }

    public void praiseNews(String newsId,int uid, int praiseNum, ArkHttpClient.HttpHandler handler) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("newsId", newsId);
            jsonObject.put("uid", uid);
            jsonObject.put("praiseNum", praiseNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpClient.post(PRAISE_NEWS, jsonObject, handler);
    }
}
