package com.skytech.moa;

import android.content.Context;
import com.skytech.android.SkyInitializer;
import com.skytech.android.util.NetworkUtils;

public class App extends SkyInitializer {

    public App(Context context) {
        super(context);
    }

    public String getNetWorkState() {
        return NetworkUtils.getConnTypeName(context);
    }
}
