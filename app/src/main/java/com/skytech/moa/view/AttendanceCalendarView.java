package com.skytech.moa.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.skytech.android.widgets.CalendarView;
import com.skytech.moa.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 考勤
 */
public class AttendanceCalendarView implements View.OnClickListener {
    public interface OnMonthClickListener {
        public void onClick(Date sdata, Date edata);

        public void onClickExceptionList();
    }

    private OnMonthClickListener dlg;
    private Activity activity;
    private CalendarView calendarView;
    private TextView yearMonth;

    public AttendanceCalendarView(Activity act, OnMonthClickListener delegated) {
        this.activity = act;
        dlg = delegated;
        calendarView = (CalendarView) act.findViewById(R.id.calendar);
        yearMonth = (TextView) act.findViewById(R.id.year_month);
        act.findViewById(R.id.arrow_left).setOnClickListener(this);
        act.findViewById(R.id.arrow_right).setOnClickListener(this);
        act.findViewById(R.id.btn_exception_list).setOnClickListener(this);
        ((TextView) act.findViewById(R.id.title)).setText("考勤记录");
    }

    public void refresh() {
        if (null != dlg) {
            dlg.onClick(calendarView.getStartDate(), calendarView.getEndDate());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_left:
                yearMonth.setText(calendarView.clickLeftMonth());
                refresh();
                break;
            case R.id.arrow_right:
                yearMonth.setText(calendarView.clickRightMonth());
                refresh();
                break;
            case R.id.btn_exception_list:
                dlg.onClickExceptionList();
                break;
        }
    }

    public void refresh(final JSONArray array) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Integer> dotColors = new HashMap<String, Integer>();
                int count = array.length();
                for (int i = 0; i < count; i++) {
                    JSONObject obj = array.optJSONObject(i);
                    if (obj.optBoolean("isexception")) {
                        dotColors.put(obj.optString("date"), Color.RED);
                    } else {
                        dotColors.put(obj.optString("date"), Color.GREEN);
                    }
                }
                calendarView.setDotColor(dotColors);
            }
        }).start();
    }
}
