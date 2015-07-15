package com.skytech.android.http;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.skytech.android.Logging;
import com.skytech.android.util.log.ArkLogger;
import org.apache.http.Header;
import org.json.JSONObject;

public interface ArkHttpClient {
    public class HttpHandler {
        public void onProgress(int bytesWritten, int totalSize) {
            ArkLogger.d(Logging.LOG_TAG, "bytesWritten:" + bytesWritten);
            ArkLogger.d(Logging.LOG_TAG, "totalSize:" + totalSize);
            ArkLogger.d("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
        }

        public void onSuccess(JSONObject response, boolean isInCache) {
            ArkLogger.d(Logging.LOG_TAG, "response:" + response.toString());
            ArkLogger.d(Logging.LOG_TAG, "isInCache:" + isInCache);
        }

        public void onFailure(String statusCode, String error) {
            ArkLogger.e(Logging.LOG_TAG, String.format("status code:%s http error:%s", statusCode, error));
        }
    }

    void setUserAgent(String userAgent);

    void downloadFile(String url, ResponseHandlerInterface handler);

    void downloadFile(String url, String fileName, final HttpHandler handler);

    void uploadFile(String filePath, String url, final HttpHandler handler, Object... args);

    void get(String url, String id, HttpHandler handler);

    void get(UrlCache url, HttpHandler handler);

    void get(String url, RequestParams params, HttpHandler handler);

    void get(UrlCache url, RequestParams params, HttpHandler handler);

    void get(UrlCache url, HttpHandler handler, Object... args);

    void get(UrlCache url, boolean isCache, HttpHandler handler, Object... args);

    void post(UrlCache url, RequestParams params, HttpHandler handler);

    void post(String url, RequestParams params, HttpHandler handler);

    void post(String url, RequestParams params, boolean isCache, HttpHandler handler);

    void post(UrlCache url, RequestParams params, HttpHandler handler, Object... args);

    void post(UrlCache url, RequestParams params, boolean isCache, HttpHandler handler, Object... args);

    void post(UrlCache url, JSONObject params, final HttpHandler handler, Object... args);

    void post(UrlCache url, JSONObject params, final boolean isCache, final HttpHandler handler, Object... args);

    void delete(UrlCache url, final HttpHandler handler);

    void delete(UrlCache url, RequestParams params, HttpHandler handler);

    void delete(final UrlCache url, Header[] headers, RequestParams params, final HttpHandler handler);

    void put(UrlCache url, JSONObject params, HttpHandler handler, Object... args);

    void execute(UrlCache url, JSONObject params, HttpHandler handler, Object... args);

    void execute(UrlCache url, RequestParams params, HttpHandler handler, Object... args);

    void execute(UrlCache url, boolean isCache, RequestParams params, HttpHandler handler, Object... args);
}
