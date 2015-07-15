package com.skytech.moa.view;

import com.skytech.moa.exception.ErrorCode;
import com.skytech.moa.exception.IllegalOperationException;

public interface ILoginView {

	public void setUserName(String userName);
	
    public String getUserName() throws IllegalOperationException;

	public void setPassword(String password);
	
    public String getPassword() throws IllegalOperationException;

    public void onStartLogin();

	public void onSuccess();

    public void onError (ErrorCode errorCode);


}
