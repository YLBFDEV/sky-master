package com.skytech.moa.services;

import com.loopj.android.http.RequestParams;
import com.skytech.android.cache.CacheType;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.android.http.UrlCache;

/**
 * Created by Administrator on 2015/6/2.
 */
public class NewsCommentService {

    private ArkHttpClient httpClient;

    private final static int PAGE_SIZE = 10;
    private int pageNum = 1;

    public static UrlCache GET_NEWS_COMMENT = new UrlCache("news/getNewsComment.do", CacheType.NOCACHE);

    public NewsCommentService() {
        httpClient = new HttpCache();
    }

    public void loadNewsComment(boolean isMore, RequestParams params, ArkHttpClient.HttpHandler handler) {

        if (isMore) {
            pageNum ++;
        } else {
            pageNum = 1;
        }

        params.put("pageSize", PAGE_SIZE + "");
        params.put("pageNumber", pageNum + "");

        httpClient.get(GET_NEWS_COMMENT, params, handler);
    }
}
