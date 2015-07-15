package com.skytech.moa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.skytech.android.ArkActivity;
import com.skytech.moa.R;
import com.skytech.moa.services.AttendanceServer;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.AttendanceCalendarView;

import java.util.Date;

/**
 * 考勤日历
 */
public class AttendanceCalendar extends ArkActivity implements AttendanceCalendarView.OnMonthClickListener {
    public AttendanceCalendarView view;
    public AttendanceServer server;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.LIST_FAILURE:
                    break;
                case Constant.LIST_SUCCESS:
                    view.refresh(server.getAttendances());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, AttendanceCalendar.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_calendar);
        view = new AttendanceCalendarView(this, this);
        server = new AttendanceServer(handler);
    }

    @Override
    public void onClick(Date sdata, Date edata) {
        server.list(sdata, edata);
    }

    @Override
    public void onClickExceptionList() {
        startActivity(GeneralList.createIntent(this, Constant.MODULE_KEY_ATTENDANCE_EXCEPTION, "异常列表"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.refresh();
    }
}
