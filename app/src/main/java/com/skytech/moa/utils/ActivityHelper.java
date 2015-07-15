package com.skytech.moa.utils;

import android.content.Context;
import android.content.Intent;
import com.skytech.moa.activity.Login;

public class ActivityHelper {
    public final static String EXTRA_OPEN_MAIN_KEY = "isOpenMain";

    public static Intent createNetSettingIntent() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }
        return intent;
    }

    public static Intent createLoginIntent(Context context) {
        Intent intent = new Intent(context, Login.class);
        intent.putExtra(EXTRA_OPEN_MAIN_KEY, false);
        return intent;
    }
}
