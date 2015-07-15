package com.skytech.moa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.skytech.android.adapter.SkyAdapter;
import com.skytech.android.draft.Draft;
import com.skytech.android.util.DateUtils;
import com.skytech.moa.R;
import org.json.JSONObject;

import java.util.List;

/**
 * 考勤异常
 */
public class AttendanceExceptionAdapter extends SkyAdapter {
    public AttendanceExceptionAdapter(Context context) {
        super(context);
    }

    @Override
    protected void draft2Json(List<Draft> drafts) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_attendance_exception, null);
        }
        JSONObject rowJson = getJSONObject(position);
        ((TextView) convertView.findViewById(R.id.exception_date)).setText(DateUtils.formatDate(rowJson.optString("date")));
        ((TextView) convertView.findViewById(R.id.state)).setText(rowJson.optString("state"));
        ((TextView) convertView.findViewById(R.id.declare)).setText(rowJson.optString("declare"));
        return convertView;
    }
}