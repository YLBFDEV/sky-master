package com.skytech.moa.presenter;

import com.skytech.moa.callback.LoginCallback;
import com.skytech.moa.exception.ErrorCode;
import com.skytech.moa.exception.IllegalOperationException;
import com.skytech.moa.services.ILoginService;
import com.skytech.moa.services.LoginService;
import com.skytech.moa.view.ILoginView;
import org.json.JSONObject;

public class LoginPresenter {
    private ILoginView view;
    private ILoginService service;

    private LoginPresenter() {
        service = new LoginService(new LoginCallback() {
            @Override
            public void onLoginStart() {
                view.onStartLogin();
            }

            @Override
            public void onLoginSuccess(JSONObject userInfo) {
                view.onSuccess();
            }

            @Override
            public void onLoginFailed(String errorMsg, ErrorCode errorCode) {
                view.onError(errorCode);

            }
        });
    }

    public LoginPresenter(ILoginView view) {
        this();
        assert (null != view);
        this.view = view;
    }

	public void init() {
		// TODO: get configuration saved and then init activity_login view, get activity_login cause etc
	}

    public void login() {
        try {
            service.login(view.getUserName(), view.getPassword());
        } catch (IllegalOperationException e) {
            view.onError(e.getErrorCode());
        }
    }
}
