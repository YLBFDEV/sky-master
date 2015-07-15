package com.skytech.android.update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import com.skytech.android.util.CustomProgress;

/**
 * Created by huangzf on 2015/2/4.
 * 系统设置 点击更新时
 */
public class DialogPrompt implements UpdateManager.UpdateHandler {
    private Context context;

    public DialogPrompt(Context context) {
        this.context = context;
    }

    @Override
    public void onBefore() {
        CustomProgress.showProgress(context, "正在检测新版本…");
    }

    @Override
    public void onNewVersion(String verName, final String apkUrl) {
        CustomProgress.hideProgress();
        StringBuffer sb = new StringBuffer();
        sb.append("发现新版本:");
        sb.append(verName);
        // sb.append(" Code:");
        // sb.append(newVerCode);
        sb.append(", 是否更新?");
        Dialog dialog = null;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Constant.ACTION_NEWVERSION);
                        intent.putExtra(Constant.EXTRA_APKURL, apkUrl);
                        context.sendBroadcast(intent);
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 点击"取消"按钮之后退出程序
                        dialogInterface.dismiss();
                    }
                });
        dialog = alertBuilder.create();
        // 不自动关闭
        dialog.setCancelable(false);
        // 显示对话框
        dialog.show();
    }

    @Override
    public void onNoNewVersion() {
        CustomProgress.hideProgress();
        Toast.makeText(context, "已是最新版本", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure() {
        CustomProgress.hideProgress();
        Toast.makeText(context, "未能获取版本信息，请检查网络", Toast.LENGTH_SHORT).show();
    }
}
