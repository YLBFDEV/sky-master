package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class DBException extends Exception {
    private static final long serialVersionUID = 1L;

    public DBException() {
        super();
    }

    public DBException(String detailMessage) {
        super(detailMessage);
    }

}
