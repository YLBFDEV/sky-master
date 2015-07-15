package com.skytech.moa.manager;

import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import org.json.JSONObject;

public class DocDetailModel {
    public interface Callback {
        public void downloadFileSuccess(String filePath);

        public void downloadFileFailure(String msg);

        public void downloadFileProgress(int progress);
    }

    private ArkHttpClient httpClient;
    private Callback dlg;

    public DocDetailModel(Callback delegate) {
        dlg = delegate;
        httpClient = HttpCache.getInstance();
    }

    public void downloadFile(String fileName) {
        fileName = "image1.png";
        String url = String.format(API.GET_FILE, fileName);
        httpClient.downloadFile(url, fileName, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                dlg.downloadFileSuccess(response.optString("filePath"));
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                dlg.downloadFileProgress((int) ((bytesWritten * 1.0 / totalSize) * 100));
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                dlg.downloadFileFailure("附件下载失败！");
            }
        });

    }
}