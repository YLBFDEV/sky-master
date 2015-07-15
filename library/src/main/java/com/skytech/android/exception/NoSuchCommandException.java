package com.skytech.android.exception;

/**
 * Description TANoSuchCommandException是当没有找到相应资源ID名字的资源时，抛出此异常！
 * @version V1.0
 * Created by yikai on 2014/11/18.
 */
public class NoSuchCommandException extends ArkException {
    private static final long serialVersionUID = 1L;

    public NoSuchCommandException()
    {
        super();
    }

    public NoSuchCommandException(String detailMessage)
    {
        super(detailMessage);

    }
}
