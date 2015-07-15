package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class DBFieldException extends ArkException
{

    private static final long serialVersionUID = -6328047121656335941L;

    public DBFieldException()
    {
    }

    public DBFieldException(String detailMessage)
    {
        super(detailMessage);
    }

}
