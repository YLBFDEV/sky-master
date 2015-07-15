package com.skytech.moa.callback;

import com.skytech.moa.exception.ErrorCode;
import org.json.JSONObject;

public interface LoginCallback {
    public void onLoginStart();
    public void onLoginSuccess(JSONObject userInfo);
    public void onLoginFailed(String errorMsg, ErrorCode errorCode);
}
