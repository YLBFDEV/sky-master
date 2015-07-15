package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class ArkException extends Exception {
    private static final long serialVersionUID= 1L;

    public ArkException() {
        super();
    }

    public ArkException(String detailMessage) {
        super(detailMessage);
    }
}
