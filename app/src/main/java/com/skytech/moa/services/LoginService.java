package com.skytech.moa.services;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.API;
import com.skytech.moa.callback.LoginCallback;
import com.skytech.moa.exception.ErrorCode;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.utils.EncryptUtil;
import org.apache.http.Header;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class LoginService implements ILoginService {
    private final static String JSESSIONID = "JSESSIONID";

    private SkyHttpClient httpClient;
    private LoginCallback callback;


    private LoginService() {
        httpClient = SkyHttpClient.getInstance();
    }


    public LoginService(LoginCallback callback) throws InvalidParameterException {
        this();
        if (null == callback) {
            throw new InvalidParameterException("callback should not be null!");
        }
        this.callback = callback;
    }


    /**
     * to activity_login
     *
     * @param userName userName
     * @param password password
     */
    public void login(String userName, final String password) {
        callback.onLoginStart();
        getRequestToken(userName, password);
    }


    /**
     * to get request token
     *
     * @param userName raw user name
     * @param password raw password
     */
    private void getRequestToken(final String userName, final String password) {
        RequestParams params = new RequestParams();
        params.put(Constant.OAUTH_REQUEST_PARAM_LOGIN_KEY, userName);
        httpClient.get(API.LOGIN_GET_REQUEST_TOKEN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String nonceToken = response.optString(Constant.OAUTH_RESPONSE_NONCE_TOKEN);
                String encryptPassword = EncryptUtil.encodePassword(userName, password, nonceToken);
                if (null != encryptPassword) {
                    getAccessToken(userName, encryptPassword);
                } else {
                    callback.onLoginFailed("get request token failed", ErrorCode.ERROR_CODE_AUTH_FAILED);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onLoginFailed("get request token failed", ErrorCode.ERROR_CODE_AUTH_FAILED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onLoginFailed("get request token failed", ErrorCode.ERROR_CODE_AUTH_FAILED);
            }
        });
    }


    /**
     * to get access token
     *
     * @param userName        raw user name
     * @param encryptPassword encrypt password
     */
    private void getAccessToken(String userName, String encryptPassword) {
        RequestParams params = new RequestParams();
        params.put(Constant.OAUTH_REQUEST_PARAM_LOGIN_KEY, userName);
        params.put(Constant.OAUTH_REQUEST_PARAM_CLIENT_SIGN_DATA, encryptPassword);
        httpClient.get(API.LOGIN_GET_ACCESS_TOKEN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                httpClient.addCookie(JSESSIONID, response.optString(Constant.OAUTH_RESPONSE_ACCESS_TOKEN));
                getUserInfo();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onLoginFailed(throwable.getMessage(), ErrorCode.ERROR_CODE_WRONG_PASSWORD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onLoginFailed(throwable.getMessage(), ErrorCode.ERROR_CODE_WRONG_PASSWORD);
            }
        });
    }


    /**
     * to get user info
     */
    private void getUserInfo() {
        httpClient.get(API.LOGIN_GET_USER_INFO, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                callback.onLoginSuccess(response.optJSONObject(Constant.OAUTH_RESPONSE_USER_INFO));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onLoginFailed(throwable.getMessage(), ErrorCode.ERROR_CODE_SERVER_EXCEPTION);
            }
        });
    }

}
