package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.moa.model.NewsCommentInfo;
import com.skytech.moa.services.NewsCommentService;
import com.skytech.moa.view.INewsCommentView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/2.
 */
public class NewsCommentPresenter {

    private final static String TAG = NewsCommentPresenter.class.getSimpleName();

    private NewsCommentService service;
    private INewsCommentView view;

    private NewsCommentInfo newsCommentInfo;

    public NewsCommentPresenter(INewsCommentView view) {
        service = new NewsCommentService();
        this.view = view;
    }

    public void loadNewsComment(final boolean isMore, RequestParams params) {
        service.loadNewsComment(isMore, params, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                ArrayList<NewsCommentInfo> newsCommentInfos = new ArrayList<NewsCommentInfo>();
                JSONArray jsonArray = response.optJSONArray("list");

                if (null == jsonArray) {
                    view.loadNewsCommentFailure("");
                    return;
                }

                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        newsCommentInfo = new NewsCommentInfo();
                        newsCommentInfo.set_id(jsonObject.getInt("id"));
                        newsCommentInfo.setUid(jsonObject.getInt("uid"));
                        newsCommentInfo.setUidPic(jsonObject.getString("uidPicUrl"));
                        newsCommentInfo.setName(jsonObject.getString("uname"));
                        newsCommentInfo.setTime(jsonObject.getString("time"));
                        newsCommentInfo.setContent(jsonObject.getString("comment"));
                        newsCommentInfos.add(newsCommentInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                view.loadNewsCommentSuccess(newsCommentInfos, isMore);
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                view.loadNewsCommentFailure(error);
            }
        });
    }

}
