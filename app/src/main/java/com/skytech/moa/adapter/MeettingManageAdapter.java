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
 * 会议管理
 */
public class MeettingManageAdapter extends SkyAdapter {
    public MeettingManageAdapter(Context context) {
        super(context);
    }

    @Override
    protected void draft2Json(List<Draft> drafts) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_meeting_manage, null);
        }
        JSONObject rowJson = getJSONObject(position);
        ((TextView) convertView.findViewById(R.id.meeting_date)).setText(DateUtils.formatDate(rowJson.optString("date")));
        ((TextView) convertView.findViewById(R.id.meeting_subject)).setText(rowJson.optString("subject"));
        ((TextView) convertView.findViewById(R.id.meeting_room)).setText(rowJson.optString("room"));
        ((TextView) convertView.findViewById(R.id.meeting_type)).setText(rowJson.optString("type"));
        ((TextView) convertView.findViewById(R.id.meeting_state)).setText(rowJson.optString("state"));
        ((TextView) convertView.findViewById(R.id.meeting_attachment)).setText(rowJson.optString("attachment"));
        return convertView;
    }
}
