package com.skytech.moa.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.model.Phone;

import java.util.List;

/**
 * Created by zhangyikai on 2015/6/3.
 */
public class PhoneAdapter extends BaseAdapter {
    private Context context;
    private List<Phone> phones;

    public PhoneAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void updateData(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public int getCount() {
        if (null == phones) {
            return 0;
        }
        return phones.size();
    }

    @Override
    public Object getItem(int position) {
        return phones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_phone, parent, false);
            holder.phoneNo = (TextView) convertView.findViewById(R.id.photo_no);
            holder.call = (ImageView) convertView.findViewById(R.id.call);
            holder.sms = (ImageView) convertView.findViewById(R.id.sms);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Phone phone = phones.get(position);
        holder.phoneNo.setText(phone.getPhone());
        holder.call.setOnClickListener(callListener(phone.getPhone()));
        holder.sms.setOnClickListener(smsListener(phone.getPhone()));
        convertView.setBackgroundResource(R.drawable.doc_field_bg);;
        return convertView;
    }

    private View.OnClickListener smsListener(final String phoneNo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms(phoneNo);
            }
        };
    }

    private View.OnClickListener callListener(final String phoneNo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(phoneNo);
            }
        };
    }

    private void sms(String phoneNo) {
        if (phoneNo == null || TextUtils.isEmpty(phoneNo.trim())) {
            CustomToast.show(getContext(), "没有电话号码");
        } else {
            Uri uri = Uri.parse("smsto:" + phoneNo);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            getContext().startActivity(sendIntent);
        }
    }

    private void call(String phoneNo) {
        if (phoneNo == null || TextUtils.isEmpty(phoneNo.trim())) {
            CustomToast.show(getContext(), "没有电话号码");
        } else {
            getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo)));
        }
    }

    class ViewHolder {
        public TextView phoneNo;
        public ImageView call;
        public ImageView sms;
    }
}
