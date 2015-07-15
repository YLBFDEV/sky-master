package com.skytech.moa.exception;

public class IllegalOperationException extends Exception {
    private ErrorCode errorCode = ErrorCode.ERROR_CODE_NONE;

    public IllegalOperationException(String msg) {
        super(msg);
    }

    public IllegalOperationException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
