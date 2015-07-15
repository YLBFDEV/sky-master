package com.skytech.moa.services;

import android.os.Handler;
import android.os.Message;
import com.skytech.android.http.SkyHttpClient;

public abstract class BaseService {
    protected SkyHttpClient httpClient;
    protected Handler handler;
    protected String error;
    /**
     * page size
     */
    protected final static String PAGE_SIZE = "20";

    /**
     * page number
     */
    protected int pageNum = 1;

    public BaseService(Handler handler) {
        this.handler = handler;
        httpClient = SkyHttpClient.getInstance();
    }

    public String getError() {
        return error;
    }

    protected void sendMsg(int what) {
        if (null != handler) {
            Message msg = new Message();
            msg.what = what;
            handler.sendMessage(msg);
        }
    }

    protected void sendMsg(int what, Object obj) {
        if (null != handler) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = obj;
            handler.sendMessage(msg);
        }
    }

    protected void sendMsg(int what, Object obj, int arg1) {
        if (null != handler) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = obj;
            msg.arg1 = arg1;
            handler.sendMessage(msg);
        }
    }
}
