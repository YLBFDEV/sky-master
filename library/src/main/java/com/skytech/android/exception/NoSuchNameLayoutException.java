package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class NoSuchNameLayoutException extends ArkException {
    private static final long serialVersionUID = 2780151262388197741L;

    public NoSuchNameLayoutException() {
        super();
    }

    public NoSuchNameLayoutException(String detailMessage) {
        super(detailMessage);
    }
}
