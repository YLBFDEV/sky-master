package com.skytech.android.exception;

public class BusinessLogicalException extends Exception {
    public BusinessLogicalException(String msg) {
        super("Business Logical Exception" + msg);
    }
}
