package com.skytech.android.exception;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

/**
 * Created by yikai on 2014/11/18.
 */
public class AppException implements Thread.UncaughtExceptionHandler
{
    public static final String TAG = "CrashHandler";
    private static AppException instance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException(Context context)
    {
        init(context);
    }

    public static AppException getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new AppException(context);
        }
        return instance;
    }

    private void init(Context context)
    {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            mDefaultHandler.uncaughtException(thread, ex);
        } else
        {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }
        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                new AlertDialog.Builder(mContext).setTitle("提示")
                        .setCancelable(false).setMessage("程序崩溃了...")
                        .setNeutralButton("我知道了", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                android.os.Process
                                        .killProcess(android.os.Process.myPid());
                                System.exit(10);
                            }
                        }).create().show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}