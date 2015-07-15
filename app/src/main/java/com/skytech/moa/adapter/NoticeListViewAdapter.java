package com.skytech.moa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.moa.R;
import com.skytech.moa.manager.NoticeManager;
import com.skytech.moa.model.NoticeInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/5.
 */
public class NoticeListViewAdapter extends BaseAdapter {

    private final static String TAG = NoticeListViewAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<NoticeInfo> noticeInfos = new ArrayList<NoticeInfo>();
    private NoticeInfo noticeInfo;

    public NoticeListViewAdapter(Context context) {
        this.context = context;
    }

    public void setNoticeInfos(ArrayList<NoticeInfo> list) {
        this.noticeInfos = list;
    }

    @Override
    public int getCount() {
        if (null == noticeInfos) {
            return 0;
        }
        return noticeInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_item, null);
            holder = new ViewHolder();
            holder.ivIsRead = (ImageView) convertView.findViewById(R.id.iv_notice_isread);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_notice_title);
            holder.tvDept = (TextView) convertView.findViewById(R.id.tv_notice_dept);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_notice_time);
            holder.ivAttachment = (ImageView) convertView.findViewById(R.id.iv_notice_hasattachment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        noticeInfo = noticeInfos.get(position);

        if (NoticeManager.getInstance().isRead(noticeInfo.getId())) {
            holder.ivIsRead.setVisibility(View.INVISIBLE);
        } else {
            holder.ivIsRead.setVisibility(View.VISIBLE);
        }

        if (noticeInfo.getHasAttachment() == true) {
            holder.ivAttachment.setVisibility(View.VISIBLE);
        }

        holder.tvTitle.setText(noticeInfo.getTitle());
        holder.tvDept.setText(noticeInfo.getDept());
        holder.tvTime.setText(noticeInfo.getReleaseTime());

        return convertView;
    }

    public class ViewHolder {
        private ImageView ivIsRead;
        private TextView tvTitle;
        private TextView tvDept;
        private TextView tvTime;
        private ImageView ivAttachment;
    }

}
