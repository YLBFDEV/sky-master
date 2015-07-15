package com.skytech.android.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Toast之解决重复显示问题
 */
public class CustomToast {

    private static Toast mToast;
    private static Handler mhandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void show(Context context, String text, int duration) {
        mhandler.removeCallbacks(r);
        if (null != mToast) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, duration);
        }
        mhandler.postDelayed(r, 5000);
        mToast.show();
    }

    public static void show(Context context, int strId, int duration) {
        show(context, context.getString(strId), duration);
    }

    public static void show(Context context, int strId) {
        show(context, context.getString(strId), 0);
    }

    public static void show(Context context, String text) {
        show(context, text, 0);
    }
}
