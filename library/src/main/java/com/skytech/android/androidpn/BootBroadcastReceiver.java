package com.skytech.android.androidpn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by huangzf on 2015/5/12.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    public static final String RECEIVER_NAME = "com.skytech.android.BootBroadcastReceiver";

    public static Intent createIntent() {
        return new Intent(RECEIVER_NAME);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LogUtil.makeLogTag(BootBroadcastReceiver.class), intent.getAction());
        //开机，网络发生变化时，启动消息推送接收服务
        context.startService(NotificationService.getIntent());
    }
}
