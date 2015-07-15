package com.skytech.android.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {
    private UpdateManager updateManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateManager = new UpdateManager(this);
        updateManager.checkUpdate(new NotificationPrompt(this));
    }
}
