package com.skytech.moa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.moa.R;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.utils.Constant;

import java.util.ArrayList;

public class NoticeAttachmentAdapter extends BaseAdapter {

    private final static String TAG = NoticeListViewAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DocumentDetail> documentDetails;
    private DocumentDetail documentDetail;

    public NoticeAttachmentAdapter(Context context) {
        this.context = context;
    }

    public void setNoticeAttachmentAdapter(ArrayList<DocumentDetail> list){
        this.documentDetails = list;
    }

    @Override
    public int getCount() {
        if (null == documentDetails) {
            return 0;
        }
        return documentDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return documentDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_attachment_list_item, null);
            holder = new ViewHolder();
            holder.ivAttachmentPic = (ImageView) convertView.findViewById(R.id.iv_notice_attachment_pic);
            holder.tvAttachmentName = (TextView) convertView.findViewById(R.id.iv_notice_attachment_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         * 这边Attachment是一个model
         */
        documentDetail = documentDetails.get(position);
        int docType = documentDetail.getDocFormat();
        switch (docType) {
            case Constant.DOC:
                holder.ivAttachmentPic.setBackgroundResource(R.drawable.doc_file_word);
                break;
            case Constant.PDF:
                holder.ivAttachmentPic.setBackgroundResource(R.drawable.doc_file_pdf);
                break;
            case Constant.PPT:
                holder.ivAttachmentPic.setBackgroundResource(R.drawable.doc_file_ppt);
                break;
            case Constant.ZIP:
                holder.ivAttachmentPic.setBackgroundResource(R.drawable.doc_file_zip);
                break;
        }

        holder.tvAttachmentName.setText(documentDetail.getName());

        return convertView;
    }

    private class ViewHolder{
        private ImageView ivAttachmentPic;
        private TextView tvAttachmentName;
    }

}
