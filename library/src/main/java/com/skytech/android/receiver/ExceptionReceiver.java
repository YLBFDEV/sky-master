package com.skytech.android.receiver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.skytech.android.util.Constant;

public class ExceptionReceiver extends BroadcastReceiver {
    private static Dialog dlg;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
        }
        String action = intent.getAction();
        if (action.equals(Constant.ACTION_NETWORK_TIMEDOUT)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("连接服务器超时，请检查网络设置！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //context.startActivity(ActivityHelper.createNetSettingIntent());
                }
            });
            dlg = builder.create();
            dlg.show();
        } else if (action.equals(Constant.ACTION_LOGIN_FAIL)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("用户失效，请重新登录！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //context.startActivity(ActivityHelper.createLoginIntent(context));
                }
            });
            dlg = builder.create();
            dlg.show();
        }
    }
}
