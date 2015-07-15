package com.skytech.moa.manager;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;

import java.io.File;

public class AttachmentModel {
    public interface Callback {
        public void downloadFileSuccess(String filePath);

        public void downloadFileFailure(String msg);

        public void downloadFileProgress(int progress);
    }

    private ArkHttpClient httpClient;
    private Callback dlg;

    public AttachmentModel(Callback delegate) {
        dlg = delegate;
        httpClient = HttpCache.getInstance();
    }

    public void downloadFile(String id, String filePath) {
        httpClient.downloadFile(String.format("%s?%s=%s&attchid=%s", API.GET_DOWNLOADFILE.getUrl(), Constant.USER_ID, App.getInstance().getUser().getId(), id),
                new FileAsyncHttpResponseHandler(new File(filePath)) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {

                        dlg.downloadFileSuccess(file.getPath());
                    }

                    @Override
                    public void onProgress(int bytesWritten, int totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        dlg.downloadFileProgress((int) ((bytesWritten * 1.0 / totalSize) * 100));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        dlg.downloadFileFailure("附件下载失败！");
                    }
                }
        );
    }
}
