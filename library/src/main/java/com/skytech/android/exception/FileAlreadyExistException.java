package com.skytech.android.exception;

/**
 * Created by yikai on 2014/11/18.
 */
public class FileAlreadyExistException extends ArkException
{
    private static final long serialVersionUID = 1L;

    public FileAlreadyExistException(String message)
    {

        super(message);
    }

}