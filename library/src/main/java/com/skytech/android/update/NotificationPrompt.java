package com.skytech.android.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.skytech.R;

/**
 * Created by huangzf on 2015/2/4.
 * 系统启动时 检测新版本
 */
public class NotificationPrompt implements UpdateManager.UpdateHandler {
    private Context context;

    public NotificationPrompt(Context context) {
        this.context = context;
    }

    @Override
    public void onBefore() {

    }

    @Override
    public void onNewVersion(String verName, String apkUrl) {
        //获取Notification管理器
        NotificationManager noteMng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //新建一个Notification
        Notification.Builder builder = new Notification.Builder(context).setTicker("更新")
                .setSmallIcon(R.drawable.ic_launcher);
        //通过Intent，使得点击Notification之后会启动新的Activity或广播
        Intent i = new Intent(Constant.ACTION_NEWVERSION);
        i.putExtra(Constant.EXTRA_APKURL, apkUrl);
        //该标志位表示如果Intent要启动的Activity在栈顶，则无须创建新的实例
        // i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification note = builder.setContentIntent(pendingIntent).setContentTitle("应用更新").setContentText("新版本为：" + verName).build();
        //FLAG_ONGOING_EVENT表明有程序在运行，该Notification不可由用户清除
        // note.flags = Notification.FLAG_ONGOING_EVENT;
        //FLAG_AUTO_CANCEL点击后自动消失
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        noteMng.notify(1024, note);
    }

    @Override
    public void onNoNewVersion() {

    }

    @Override
    public void onFailure() {

    }
}
