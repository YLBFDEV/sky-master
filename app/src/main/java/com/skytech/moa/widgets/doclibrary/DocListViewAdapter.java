package com.skytech.moa.widgets.doclibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.moa.R;
import com.skytech.moa.model.Document;


public class DocListViewAdapter extends DocBaseAdapter {
    private LayoutInflater inflater;
    private Document document;

    public DocListViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        document = getDocLibraries().get(position);
        if (null == view) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.docs_listview_item, null);
            viewHolder.docName = (TextView) view.findViewById(R.id.doc_folder_name);
            viewHolder.createTime = (TextView) view.findViewById(R.id.create_time);
            viewHolder.number = (TextView) view.findViewById(R.id.doc_num);
            viewHolder.folderImage = (ImageView) view.findViewById(R.id.doc_folder_image);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (position == 0) {
            viewHolder.folderImage.setBackgroundResource(R.drawable.doc_ic_folder_favorites);
        }
        if (document.getId() != -1) {
            viewHolder.docName.setText(document.getName());
            viewHolder.createTime.setText(document.getCreateTime());
            viewHolder.number.setText(document.getNumber() + "");
        } else {
            (view.findViewById(R.id.doc_list_item_normal)).setVisibility(View.GONE);
            (view.findViewById(R.id.doc_list_item_last)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.docs_list_bottom_line)).setVisibility(View.GONE);
        }
        view.setTag(viewHolder);
        return view;
    }


    private final class ViewHolder {
        public TextView docName;
        public TextView number;
        public TextView createTime;
        public ImageView folderImage;
    }

}
