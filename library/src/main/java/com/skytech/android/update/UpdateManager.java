package com.skytech.android.update;

import android.content.Context;
import android.content.pm.PackageManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.R;
import org.apache.http.Header;
import org.json.JSONObject;

public class UpdateManager {

    public interface UpdateHandler {
        public void onBefore();

        public void onNewVersion(String verName, String apkUrl);

        public void onNoNewVersion();

        public void onFailure();
    }

    private Context mContext;
    private int newVerCode = 0;
    private String newVerName = "";
    private AsyncHttpClient httpClient;

    public UpdateManager(Context context) {
        this.mContext = context;
        httpClient = new AsyncHttpClient();
    }

    public void checkUpdate() {
        checkUpdate(new DialogPrompt(mContext));
    }

    public void checkUpdate(final UpdateHandler handler) {
        handler.onBefore();
        httpClient.get(getVersionUrl(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                newVerName = response.optString("vername");
                newVerCode = response.optInt("vercode");
                String apkurl = response.optString("apkurl");
                if (newVerCode > getLocalVerCode()) {
                    handler.onNewVersion(newVerName, apkurl);
                } else {
                    handler.onNoNewVersion();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                handler.onFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handler.onFailure();
            }
        });
    }

    private int getLocalVerCode() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String getVersionUrl() {
        return mContext.getString(R.string.update_url);
    }
}
