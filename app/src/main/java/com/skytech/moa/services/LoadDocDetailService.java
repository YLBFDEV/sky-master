package com.skytech.moa.services;

import com.skytech.android.cache.CacheType;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.android.http.UrlCache;
import com.skytech.moa.API;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.view.IDocDetailView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadDocDetailService {
    private ArrayList<DocumentDetail> documentDetails;
    private IDocDetailView iDocDetailView;
    private ArkHttpClient httpClient;
    private static final int pageSize = 10;
    private static int pagetNumber = 1;
    private JSONObject jsonObject;

    public LoadDocDetailService(IDocDetailView iDocDetailView) {
        this.iDocDetailView = iDocDetailView;
        httpClient = new HttpCache();
    }

    public void loadDocDetails(int uid, int id, final boolean isMore) {
        if (isMore) {
            pagetNumber++;
        } else {
            pagetNumber = 1;
        }
        try {
            jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonObject.put("id", id);
            jsonObject.put("pageNumber", pagetNumber);
            jsonObject.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpClient.post(API.GET_ALL_DOC_DETAILS, jsonObject, new ArkHttpClient.HttpHandler() {

                    @Override
                    public void onSuccess(JSONObject response, boolean isInCache) {
                        try {
                            documentDetails = new ArrayList<>();
                            String msg = response.getString("msg");
                            Boolean success = response.getBoolean("success");
                            JSONArray list = response.getJSONArray("list");
                            if (success) {
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject documentDetail = ((JSONObject) list.opt(i));
                                    documentDetails.add(new DocumentDetail(documentDetail));
                                }
                            } else {
                                System.out.println("加载失败");
                            }

                            if (isMore) {
                                iDocDetailView.more(documentDetails);
                            } else {
                                iDocDetailView.loadAllDocDetail(documentDetails);
                            }
                        } catch (JSONException e) {
                            System.out.println("Json parse error");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String statusCode, String error) {
                        System.out.println(API.GET_ALL_DOCUMENTS);

                        iDocDetailView.failure(error);
                    }
                }
        );
    }

    public void pullToRefresh(int uid, int id) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonObject.put("id", id);
            jsonObject.put("pageNumber", 1);
            jsonObject.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpClient.post(API.GET_ALL_DOC_DETAILS,jsonObject, new ArkHttpClient.HttpHandler() {
                    @Override
                    public void onSuccess(JSONObject response, boolean isInCache) {
                        try {
                            documentDetails = new ArrayList<>();
                            String msg = response.getString("msg");
                            Boolean success = response.getBoolean("success");
                            JSONArray list = response.getJSONArray("list");
                            if (success) {
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject documentDetail = ((JSONObject) list.opt(i));
                                    documentDetails.add(new DocumentDetail(documentDetail));
                                }
                            } else {
                                System.out.println("加载失败");
                            }
                            iDocDetailView.pullToRefresh(documentDetails);
                        } catch (JSONException e) {
                            System.out.println("Json parse error");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(String statusCode, String error) {
                        System.out.println(API.GET_ALL_DOCUMENTS);
                        iDocDetailView.failure(error);
                    }
                }
        );
    }
}
