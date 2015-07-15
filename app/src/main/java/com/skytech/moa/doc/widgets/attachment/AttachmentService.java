package com.skytech.moa.doc.widgets.attachment;

import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import org.json.JSONObject;

import java.util.List;

public class AttachmentService {
    private ArkHttpClient httpClient;
    private IFieldAttachment view;
    private int uploadFileNum;
    private String result;


    public AttachmentService(IFieldAttachment view) {
        httpClient = new HttpCache();
        this.view = view;
    }

    public void upload(final List<String> filePath) {
        result = "";
        uploadFileNum = 0;
        for (final String path : filePath) {
            if (null == path) {
                uploadFileNum++;
                continue;
            }
            httpClient.uploadFile(path, API.POST_FILE, new ArkHttpClient.HttpHandler() {
                @Override
                public void onSuccess(JSONObject response, boolean isInCache) {
                    super.onSuccess(response, isInCache);
                    uploadFileNum++;
                    if (result.isEmpty()) {
                        result = response.optString("_id");
                    } else {
                        result += "," + response.optString("_id");
                    }

                    if (uploadFileNum == filePath.size()) {
                        view.onUploadSuccess(result);
                    }
                }

                @Override
                public void onFailure(String statusCode, String error) {
                    super.onFailure(statusCode, error);
                    uploadFileNum++;
                    view.onUploadFailure();
                }
            });
        }
    }

    public void downLoad(String id, final int position) {
        httpClient.get(API.GET_FILE, id, new ArkHttpClient.HttpHandler() {
            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                double speedF = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;
                view.onDownloadProgress(position, speedF + "");
            }

            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                view.onDownloadSuccess(position, response.optString("filePath"));
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                view.onDownloadFailure(position, "error");
            }
        });
    }
}