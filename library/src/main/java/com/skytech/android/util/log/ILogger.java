package com.skytech.android.util.log;

/**
 * @Description Logger是一个日志的接口
 * Created by yikai on 2014/11/19.
 */
public interface ILogger {
    void v(String tag, String message);

    void d(String tag, String message);

    void i(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message, Throwable tr);

    void open();

    void close();

    void println(int priority, String tag, String message);
}
