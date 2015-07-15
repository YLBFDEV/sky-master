package com.skytech.android.util.log;

import android.util.Log;

/**
 * @Description PrintToLogCatLogger是框架中打印到LogCat上面的日志类
 * Created by yikai on 2014/11/19.
 */
public class PrintToLogCatLogger implements ILogger {
    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void e(String tag, String message, Throwable tr) {
        Log.e(tag, message, tr);
    }

    @Override
    public void i(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public void v(String tag, String message) {
        Log.v(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        Log.w(tag, message);
    }

    @Override
    public void println(int priority, String tag, String message) {
        Log.println(priority, tag, message);
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }
}