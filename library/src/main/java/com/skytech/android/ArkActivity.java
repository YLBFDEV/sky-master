package com.skytech.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.skytech.android.receiver.ExceptionReceiver;
import com.skytech.android.util.Constant;
import com.skytech.android.util.netstate.NetType;
import com.skytech.R;

public class ArkActivity extends FragmentActivity {
    private BroadcastReceiver mExceptionReceiver = new ExceptionReceiver();

    public void finish(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        try {
            ((TextView) findViewById(R.id.title)).setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_NETWORK_TIMEDOUT);
        intentFilter.addAction(Constant.ACTION_LOGIN_FAIL);
        registerReceiver(mExceptionReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mExceptionReceiver);
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetType type) {
    }

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
