package com.skytech.android.util;

import android.content.Context;


public class DisplayUtil {
    private static DisplayUtil screenTools;
    private Context context;

    public DisplayUtil(Context context) {
        this.context = context;
    }

    public static DisplayUtil getInstance(Context context) {
        if (null == screenTools) {
            screenTools = new DisplayUtil(context);
        }
        return screenTools;
    }

    public int dip2px(float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getDip(int id) {
        return px2dip(context.getResources().getDimension(id));
    }
}
