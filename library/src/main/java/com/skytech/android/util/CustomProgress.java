package com.skytech.android.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.skytech.R;

/**
 * custom progress extend {@link android.app.Dialog}
 */
public class CustomProgress {

    private static Dialog dlg;

    public static void showProgress(Context context) {
        showProgress(context, "");
    }

    public static void showProgress(Context context, int resid) {
        showProgress(context, context.getString(resid));
    }

    public static void showProgress(Context context, String text) {
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
        }
        dlg = new Dialog(context, R.style.loading_dialog);
       /* if (dlg.isShowing()) {
            return;
        }*/
        // dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // home_pad.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(text);// 设置加载信息

        //dlg.setMessage(text);
        dlg.setCancelable(false);
        dlg.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });
        dlg.show();
    }

    public static void hideProgress() {
        if (null != dlg) dlg.dismiss();
    }
}
