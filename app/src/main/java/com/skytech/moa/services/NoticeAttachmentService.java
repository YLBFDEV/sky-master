package com.skytech.moa.services;

import android.util.Log;
import com.loopj.android.http.RequestParams;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.view.INoticesListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeAttachmentService {

    private static final String TAG = NoticeAttachmentService.class.getSimpleName();

    private ArkHttpClient httpClient;
    private INoticesListView view;

    public NoticeAttachmentService(INoticesListView iNoticesListView) {
        httpClient = new HttpCache();
        this.view = iNoticesListView;
    }

    public void getNoticeAttachment(String noticeId) {
        Log.i(TAG, "............................NoticeAttachmentService getNoticeAttachment()");
        RequestParams params = new RequestParams();
        params.put("noticeid", noticeId);
        httpClient.get(API.GET_NOTICE_ATTACHMENT, params, new ArkHttpClient.HttpHandler(){
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                ArrayList<DocumentDetail> documentDetails = new ArrayList<DocumentDetail>();
                try {
                    JSONArray list = response.getJSONArray("list");
                    Boolean success = response.getBoolean("success");
                    int length = list.length();
                    if (success) {
                        for (int i = 0; i < length; i++) {
                            JSONObject documentDetail = ((JSONObject) list.opt(i));
                            documentDetails.add(new DocumentDetail(documentDetail));
                        }
                    } else {
                        System.out.println("加载失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                view.loadNoticeAttachmentSuccess(documentDetails);
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                view.loadNoticeAttachmentFailure(error);
            }
        });
    }

}
