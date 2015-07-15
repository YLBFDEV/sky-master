package com.skytech.moa.services;

import com.skytech.android.cache.CacheType;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.android.http.UrlCache;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/3.
 */
public class NewsCommentCommitService {

    private ArkHttpClient httpClient;
    private JSONObject jsonObject;

    public static UrlCache COMMIT_COMMENT = new UrlCache("news/commitComment.do", CacheType.NOCACHE);

    public NewsCommentCommitService() {
        httpClient = new HttpCache();

    }

    public void commitComment(String newsId, int uid, String uname, String uPicUrl, String comment, ArkHttpClient.HttpHandler handler) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("newsId", newsId);
            jsonObject.put("uid", uid);
            jsonObject.put("uname", uname);
            jsonObject.put("uPicUrl", uPicUrl);
            jsonObject.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpClient.post(COMMIT_COMMENT, jsonObject, handler);
    }
}
