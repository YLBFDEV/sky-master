package com.skytech.moa.widgets.doclibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.moa.R;
import com.skytech.moa.model.Document;

public class DocGridViewAdapter extends DocBaseAdapter {
    private LayoutInflater inflater;
    private Document document;
    private ViewHolder viewHolder;

    public DocGridViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        document = getDocLibraries().get(i);
        view = initView(view);
        if (i == 0) {
            viewHolder.folderImage.setBackgroundResource(R.drawable.doc_ic_folder_favorites);
        }
        if (document.getId() == -1) {
            (view.findViewById(R.id.doc_grid_relativeLayout)).setVisibility(View.GONE);
            (view.findViewById(R.id.doc_grid_last_relativeLayout)).setVisibility(View.VISIBLE);

        } else {
            viewHolder.docName.setText(document.getName());
            viewHolder.number.setText(document.getNumber() + "");
        }
        view.setTag(viewHolder);
        return view;
    }

    public View initView(View view) {
        if (null == view) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.docs_gridview_item, null);
            viewHolder.docName = (TextView) view.findViewById(R.id.doc_folder_name);
            viewHolder.number = (TextView) view.findViewById(R.id.doc_num);
            viewHolder.folderImage = (ImageView) view.findViewById(R.id.doc_folder_image);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private final class ViewHolder {
        public TextView docName;
        public TextView number;
        public ImageView folderImage;
    }
}
