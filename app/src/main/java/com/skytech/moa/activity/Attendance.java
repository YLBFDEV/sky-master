package com.skytech.moa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.baidu.mapapi.SDKInitializer;
import com.skytech.android.ArkActivity;
import com.skytech.android.Logging;
import com.skytech.moa.R;
import com.skytech.moa.manager.AttendanceManager;
import com.skytech.moa.view.AttendanceView;

/**
 * 考勤签到
 */
public class Attendance extends ArkActivity implements AttendanceView.OnClickListener {
    private AttendanceManager manager;
    private AttendanceView view;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, Attendance.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);
        view = new AttendanceView(this, this);
        manager = new AttendanceManager(this, view, handler);

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        registerReceiver(mReceiver, iFilter);
    }

    @Override
    protected void onPause() {
        view.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        view.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        manager.onDestroy();
        view.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.e(Logging.LOG_TAG, "key 验证出错!");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Log.e(Logging.LOG_TAG, "网络出错!");
            }
        }
    };

    @Override
    public void onClickSignIn(int type, double latitude, double longitude, String address) {
        manager.signIn(type, latitude, longitude, address);
    }

    @Override
    public void onClickRecord() {
        startActivity(AttendanceCalendar.createIntent(this));
    }
}
