package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import com.baidu.mapapi.SDKInitializer;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.App;
import com.skytech.moa.R;

public class Welcome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SDKInitializer.initialize(getApplicationContext());
                App.initialize(getApplicationContext());
                SkyHttpClient.initialize(getApplicationContext());
                new Handler().postDelayed(new startLoginActivity(), 1000);
            }
        }).run();
    }

    private class startLoginActivity implements Runnable {
        @Override
        public void run() {
            startActivity(Login.createIntent(Welcome.this));
            // 关闭当前界面
            finish();
        }
    }
}
