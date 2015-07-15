package com.skytech.moa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import com.skytech.android.ArkActivity;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.exception.ErrorCode;
import com.skytech.moa.exception.IllegalOperationException;
import com.skytech.moa.presenter.LoginPresenter;
import com.skytech.moa.view.ILoginView;

public class Login extends ArkActivity implements ILoginView {
    public static Intent createIntent(Context context) {
        return new Intent(context, Login.class);
    }
    private LoginPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);
        presenter.init();
    }

    public void login(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
        //presenter.login();
    }


    @Override
    public void setUserName(String userName) {
        ((EditText) findViewById(R.id.user_name)).setText(userName);
    }

    @Override
    public String getUserName() throws IllegalOperationException {
        //TODO: use regex to do verification
        String userName = ((EditText) findViewById(R.id.user_name)).getText().toString().trim();
        if (!TextUtils.isEmpty(userName)) {
            return userName;
        } else {
            throw new IllegalOperationException(getString(R.string.user_name_empty_error_hint),
                    ErrorCode.ERROR_CODE_EMPTY_USER_NAME);
        }
    }

    @Override
    public void setPassword(String password) {
        ((EditText) findViewById(R.id.password)).setText(password);
    }


    @Override
    public String getPassword() throws IllegalOperationException {
        //TODO: use regex to do verification
        String userName = ((EditText) findViewById(R.id.password)).getText().toString().trim();
        if (!TextUtils.isEmpty(userName)) {
            return userName;
        } else {
            throw new IllegalOperationException(getString(R.string.password_empty_error_hint),
                    ErrorCode.ERROR_CODE_EMPTY_PASSWORD);
        }
    }


    @Override
    public void onStartLogin() {
        CustomProgress.showProgress(this);
    }


    @Override
    public void onSuccess() {
        startActivity(new Intent(this, Home.class));
        finish();
    }


    @Override
    public void onError(ErrorCode errorCode) {
        CustomProgress.hideProgress();
        switch (errorCode) {
            case ERROR_CODE_EMPTY_USER_NAME:
                toast(getString(R.string.user_name_empty_error_hint));
                break;
            case ERROR_CODE_EMPTY_PASSWORD:
                toast(getString(R.string.password_empty_error_hint));
                break;
            case ERROR_CODE_ILLEGAL_USER_NAME:
                toast(getString(R.string.illegal_user_name_error_hint));
                break;
            case ERROR_CODE_ACCOUNT_NOT_EXIST:
                toast(getString(R.string.account_not_exist_error_hint));
                break;
            case ERROR_CODE_WRONG_PASSWORD:
                toast(getString(R.string.wrong_password_error_hint));
                break;
            case ERROR_CODE_NO_NETWORK:
                toast(getString(R.string.no_network_error_hint));
                break;
            case ERROR_CODE_SERVER_EXCEPTION:
                toast(getString(R.string.server_exception_error_hint));
            default:
                toast(String.format(getString(R.string.undefined_error), errorCode));

        }
    }


    private void toast(String msg) {
        CustomToast.show(this, msg);
    }
}
