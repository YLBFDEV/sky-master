package com.skytech.android.http;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.skytech.android.Logging;
import com.skytech.android.cache.CacheType;
import com.skytech.android.util.PathUtil;
import com.skytech.android.util.log.ArkLogger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

public class HttpCache implements ArkHttpClient {
    private static final String USER_AGENT = "ArkHttpClient";
    private static final String SUCCESS_FLAG = "success";
    private static final String ERROR_CODE_FLAG = "error_code";
    private static final String ERROR_MSG_FLAG = "message";

    public static String SERVER_EXCEPTION = "800";
    public static String RESPONSE_JSON_EXCEPTION = "801";

    private static ArkHttpClient arkHttpClient;

    private SkyHttpClient httpClient;
    private static final int SOCKET_TIMEOUT = 50 * 1000;

    public HttpCache() {
        if (null == httpClient) {
            httpClient = SkyHttpClient.getInstance();
            httpClient.setTimeout(SOCKET_TIMEOUT);
            httpClient.setUserAgent(USER_AGENT);
        }
    }

    public static ArkHttpClient getInstance() {
        if (null == arkHttpClient) {
            arkHttpClient = new HttpCache();
        }
        return arkHttpClient;
    }

    @Override
    public void setUserAgent(String userAgent) {
        httpClient.setUserAgent(userAgent);
    }

    private JSONObject prepareData(String filePath) {
        JSONObject fileData = new JSONObject();
        try {
            fileData.put("filePath", filePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    @Override
    public void downloadFile(String url, ResponseHandlerInterface handler) {
        httpClient.get(url, handler);
    }

    @Override
    public void downloadFile(String url, String fileName, final HttpHandler handler) {
        String filePath = PathUtil.getInstance().getAttachmentDir() + "/" + fileName;
        File wdFile = new File(filePath);
        List files = ConfigCache.findFiles(PathUtil.getInstance().getAttachmentDir(), fileName, 1);
        if (files.size() > 0) {//文件已存在
            handler.onSuccess(prepareData(files.get(0).toString()), true);
            return;
        }
        downloadFile(url, new FileAsyncHttpResponseHandler(wdFile) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                handler.onSuccess(prepareData(response.getPath()), false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                if (file.exists() && file.isFile())
                    file.delete();
                handler.onFailure("" + statusCode, "服务器错误,附件下载失败！");
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                handler.onProgress(bytesWritten, totalSize);
            }
        });
    }

    @Override
    public void uploadFile(String filePath, String url, final HttpHandler handler, Object... args) {
        final String strUrl = String.format(url, args);
        RequestParams params = new RequestParams();
        try {
            params.put("file", new File(filePath)); // Upload a File
            //params.put("profile_picture2", someInputStream); // Upload an InputStream
            //params.put("profile_picture3", new ByteArrayInputStream(someBytes)); // Upload some bytes
        } catch (FileNotFoundException e) {
            ArkLogger.e(Logging.LOG_TAG, e.getMessage());
        }
        httpClient.post(strUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, true, handler, "");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, false, "");
            }
        });
    }

    @Override
    public void get(String url, String id, final HttpHandler handler) {
        final String filePath = PathUtil.getInstance().getAttachmentDir() + "/" + id;
        final File wdFile = new File(filePath);

        List files = ConfigCache.findFiles(PathUtil.getInstance().getAttachmentDir(), id, 1);
        if (files.size() > 0) {//文件已存在
            handler.onSuccess(prepareData(files.get(0).toString()), true);
            return;
        }

        url = String.format(url, id);
        httpClient.get(url, new FileAsyncHttpResponseHandler(wdFile) {
            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                handler.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                String fileName = headers[1].getValue().split(";")[1].split("=")[1];
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                if (fileName.equals("file.no")) {//服务器未找到附件
                    wdFile.delete();
                    handler.onSuccess(prepareData(fileName), false);
                } else {
                    wdFile.renameTo(new File(filePath + "." + fileType));
                    handler.onSuccess(prepareData(filePath + "." + fileType), false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                handler.onFailure("", "服务器错误,附件下载失败！");
            }
        });
    }

    @Override
    public void get(UrlCache url, HttpHandler handler) {
        get(url, true, handler);
    }

    @Override
    public void get(final String url, RequestParams params, final HttpHandler handler) {
        final String cacheFileName = getCacheFileName(url, params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http cacheFileName:" + cacheFileName);
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, false, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, true, handler, cacheFileName);
            }
        });
    }

    public void get(final UrlCache url, final boolean isCache, RequestParams params, final HttpHandler handler) {
        final String cacheFileName = getCacheFileName(url.getUrl(), params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http cacheFileName:" + cacheFileName);

        if (isCache && readFormCache(url.getCacheType(), handler, cacheFileName)) return;

        httpClient.get(url.getUrl(), params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, isCache, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, true, handler, cacheFileName);
            }
        });
    }

    @Override
    public void get(UrlCache url, RequestParams params, HttpHandler handler) {
        get(url, true, params, handler);
    }

    @Override
    public void get(UrlCache url, HttpHandler handler, Object... args) {
        get(url, true, handler, args);
    }

    @Override
    public void get(final UrlCache url, final boolean isCache, final HttpHandler handler, Object... args) {
        final String strUrl = (args.length == 0 ? url.getUrl() : String.format(url.getUrl(), args));
        final String cacheFileName = getCacheFileName(strUrl, null);
        ArkLogger.d(Logging.LOG_TAG, "http cacheFileName:" + cacheFileName);

        if (isCache && readFormCache(url.getCacheType(), handler, cacheFileName)) return;

        //用户刷新，更多不取缓存
        //如果缓存结果是空,需要重新加载
        //缓存为空的原因可能是1.无缓存;2. 缓存过期;3.读取缓存出错
        httpClient.get(null, strUrl, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, url.getCacheType().equals(CacheType.NOCACHE) == false, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, isCache, handler, cacheFileName);
            }
        });
    }

    @Override
    public void post(UrlCache url, RequestParams params, HttpHandler handler) {
        post(url, params, false, handler);
    }

    @Override
    public void post(String url, RequestParams params, HttpHandler handler) {
        post(url, params, false, handler);
    }

    @Override
    public void post(UrlCache url, RequestParams params, HttpHandler handler, Object... args) {
        post(url, params, true, handler, args);
    }

    @Override
    public void post(UrlCache url, JSONObject params, final HttpHandler handler, Object... args) {
        post(url, params, false, handler, args);
    }

    @Override
    public void post(UrlCache url, JSONObject params, final boolean isCache, final HttpHandler handler, Object... args) {
        String strUrl = (args.length == 0 ? url.getUrl() : String.format(url.getUrl(), args));
        final String cacheFileName = getCacheFileName(strUrl, params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http cacheFileName:" + cacheFileName);

        if (isCache && readFormCache(CacheType.NOCACHE, handler, cacheFileName)) return;

        httpClient.post(null, strUrl, Json2Entity(params), null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, isCache, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, isCache, handler, cacheFileName);
            }
        });
    }

    @Override
    public void post(UrlCache url, RequestParams params, boolean isCache, HttpHandler handler, Object... args) {
        String strUrl = (args.length == 0 ? url.getUrl() : String.format(url.getUrl(), args));
        post(strUrl, params, isCache, handler);
    }

    @Override
    public void post(final String url, RequestParams params, final boolean isCache, final HttpHandler handler) {
        final String cacheFileName = getCacheFileName(url, params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http cacheFileName:" + cacheFileName);

        if (isCache && readFormCache(CacheType.NOCACHE, handler, cacheFileName)) return;

        httpClient.post(null, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, isCache, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, isCache, handler, cacheFileName);
            }
        });
    }

    @Override
    public void delete(UrlCache url, final HttpHandler handler) {
        delete(url, null, null, handler);
    }

    @Override
    public void delete(UrlCache url, RequestParams params, final HttpHandler handler) {
        delete(url, null, params, handler);
    }

    @Override
    public void delete(final UrlCache url, Header[] headers, RequestParams params, final HttpHandler handler) {
        final String strUrl = url.getUrl();
        final String cacheFileName = getCacheFileName(strUrl, params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());
        httpClient.delete(null, strUrl, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, false, handler, cacheFileName);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, false, cacheFileName);
            }
        });
    }

    @Override
    public void put(final UrlCache url, JSONObject params, final HttpHandler handler, Object... args) {
        final String strUrl = (args.length == 0 ? url.getUrl() : String.format(url.getUrl(), args));
        final String cacheFileName = getCacheFileName(strUrl, params.toString());
        ArkLogger.d(Logging.LOG_TAG, "http params:" + params.toString());

        httpClient.put(null, strUrl, Json2Entity(params), null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpCache.this.onSuccess(responseString, handler, url.getCacheType().equals(CacheType.NOCACHE) == false, cacheFileName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpCache.this.onFailure(statusCode, throwable, false, handler, cacheFileName);
            }
        });
    }

    @Override
    public void execute(UrlCache url, JSONObject params, HttpHandler handler, Object... args) {
        if (url.getHttpMethods().equals(HttpMethods.GET)) {
            get(url, handler, args);
        } else if (url.getHttpMethods().equals(HttpMethods.POST)) {
            post(url, params, handler, args);
        } else if (url.getHttpMethods().equals(HttpMethods.PUT)) {
            put(url, params, handler, args);
        } else {
            delete(url, handler);
        }
    }

    @Override
    public void execute(UrlCache url, RequestParams params, HttpHandler handler, Object... args) {

    }

    @Override
    public void execute(UrlCache url, boolean isCache, RequestParams params, HttpHandler handler, Object... args) {
        if (url.getHttpMethods().equals(HttpMethods.GET)) {
            get(url, isCache, handler, args);
        } else if (url.getHttpMethods().equals(HttpMethods.POST)) {
            post(url, params, isCache, handler, args);
        } else if (url.getHttpMethods().equals(HttpMethods.PUT)) {
            //put(url, params, handler, args);
        } else {
            delete(url, params, handler);
        }
    }

    /**
     * read data form cache
     *
     * @param cacheType
     * @param handler
     * @param cacheFileName
     * @return
     */
    private boolean readFormCache(CacheType cacheType, HttpHandler handler, String cacheFileName) {
        String result = ConfigCache.getCacheByUrlOfTimeout(cacheType, cacheFileName);
        //String result = ConfigCache.getFromAssets(mContext, cacheFileName);
        if (result != null) {
            try {
                handler.onSuccess(new JSONObject(result), true);
                return true;
            } catch (JSONException e) {
                ArkLogger.e(Logging.LOG_TAG, e.getMessage());
            }
        }
        return false;
    }

    private void onSuccess(String responseString, HttpHandler handler, boolean isCache, String cacheFileName) {
        JSONObject response = null;
        try {
            response = new JSONObject(responseString);
        } catch (JSONException e) {
            handler.onFailure(RESPONSE_JSON_EXCEPTION, responseString);
            return;
        }
        if (response.optBoolean(SUCCESS_FLAG)) {//返回成功
            if (isCache) {
                ConfigCache.setUrlCache(response.toString(), cacheFileName);
            }
            handler.onSuccess(response, false);
        } else {
            handler.onFailure(response.optString(ERROR_CODE_FLAG, SERVER_EXCEPTION), response.optString(ERROR_MSG_FLAG, "服务器错误！"));
            ArkLogger.e(Logging.LOG_TAG, response.toString());
        }
    }

    private void onFailure(int statusCode, Throwable throwable, boolean isCache, HttpHandler handler, String cacheFileName) {
        if (false == isCache) {
            if (throwable instanceof SocketTimeoutException) {
                handler.onFailure("" + statusCode, "服务器请求超时");
            } else if (throwable instanceof ConnectException) {
                handler.onFailure("" + statusCode, "服务器连接异常");
            } else {
                handler.onFailure("" + statusCode, throwable == null ? "error" : throwable.getMessage());
            }
        } else {
            JSONObject result = ConfigCache.getCacheByUrl(cacheFileName);
            if (result != null) {
                handler.onSuccess(result, true);
            } else {
                handler.onFailure("" + statusCode, throwable == null ? "error" : throwable.toString());
            }
        }
    }

    private HttpEntity Str2Entity(String params) {
        StringEntity stringEntity = null;
        if (params != null) {
            try {
                stringEntity = new StringEntity(params, "UTF-8");
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (Exception e) {
                ArkLogger.e(Logging.LOG_TAG, e.getMessage());
            }
        }
        return stringEntity;
    }

    private HttpEntity Json2Entity(JSONObject params) {
        StringEntity stringEntity = null;
        if (params != null) {
            try {
                stringEntity = new StringEntity(params.toString(), "UTF-8");
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (Exception e) {
                ArkLogger.e(Logging.LOG_TAG, e.getMessage());
            }
        }
        return stringEntity;
    }

    private static String getCacheFileName(String url, String prams) {
        if (null == url) {
            return null;
        }
        String fileName = url;
        if (null != prams) {
            fileName += prams.toString();
        }
        return getCacheDecodeString(fileName);
    }

    private static String getCacheDecodeString(String fileName) {
        if (fileName != null) {
            return fileName.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
        return null;
    }

}

