package com.skytech.moa.services;

import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import com.skytech.moa.model.Document;
import com.skytech.moa.view.IDocsView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadDocsService {
    private IDocsView iDocsView;
    public ArrayList<Document> documents;
    private ArkHttpClient httpClient;

    public LoadDocsService(IDocsView iDocsView) {
        httpClient = new HttpCache();
        this.iDocsView = iDocsView;
    }


    public void loadAllDocs(JSONObject jsonObject) {
        httpClient.post(API.GET_ALL_DOCUMENTS, jsonObject, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                try {
                    documents = new ArrayList<Document>();
                    String msg = response.getString("msg");
                    Boolean success = response.getBoolean("success");
                    JSONArray list = response.getJSONArray("list");
                    if (success) {
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject document = ((JSONObject) list.opt(i));
                            int id = document.getInt("id");
                            int num = document.getInt("num");
                            String name = document.getString("name");
                            String createTime = document.getString("createTime");
                            int type = document.getInt("type");
                            documents.add(new Document(id, name, type, createTime, num));
                        }
                    } else {
                        System.out.println("加载失败");
                    }
                    iDocsView.LoadAllDocuments(documents);
                } catch (JSONException e) {
                    System.out.println("Json parse error");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                iDocsView.failure(error);
            }
        });
    }



}
