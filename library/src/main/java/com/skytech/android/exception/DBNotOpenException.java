package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class DBNotOpenException extends Exception
{
    private static final long serialVersionUID = 1L;

    public DBNotOpenException()
    {
        super();
    }

    public DBNotOpenException(String detailMessage)
    {
        super(detailMessage);
    }

}
