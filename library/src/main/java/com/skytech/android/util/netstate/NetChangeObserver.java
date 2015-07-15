package com.skytech.android.util.netstate;

/**
 * @Description 是检测网络改变的观察者
 * <p/>
 * Created by yikai on 2014/11/19.
 */
public class NetChangeObserver {
    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetType type) {
    }

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {
    }
}
