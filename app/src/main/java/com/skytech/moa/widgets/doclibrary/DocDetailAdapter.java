package com.skytech.moa.widgets.doclibrary;

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

public class DocDetailAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<DocumentDetail> docDetails;
    private DocumentDetail docDetailBean;
    private ViewHolder viewHolder;

    public DocDetailAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setDocDetails(ArrayList<DocumentDetail> docDetails) {
        this.docDetails = docDetails;
    }

    @Override
    public int getCount() {
        return docDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return docDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = initView(view);
        initData(docDetails.get(position));
        action(position);
        view.setTag(viewHolder);
        return view;
    }

    public View initView(View view) {
        if (null == view) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.detail_doc_item, null);
            viewHolder.detailDocName = (TextView) view.findViewById(R.id.doc_detail_name);
            viewHolder.publisher = (TextView) view.findViewById(R.id.doc_publisher);
            viewHolder.releasetime = (TextView) view.findViewById(R.id.doc_release_time);
            viewHolder.isfavorited = (ImageView) view.findViewById(R.id.doc_isfavorited);
            viewHolder.docTypeImage = (ImageView) view.findViewById(R.id.doc_type_image);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    public void initData(DocumentDetail docDetailBean) {
        viewHolder.detailDocName.setText(docDetailBean.getName());
        switch (docDetailBean.getDocFormat()) {
            case Constant.DOC:
                viewHolder.docTypeImage.setBackgroundResource(R.drawable.doc_file_word);
                break;
            case Constant.PDF:
                viewHolder.docTypeImage.setBackgroundResource(R.drawable.doc_file_pdf);
                break;
            case Constant.PPT:
                viewHolder.docTypeImage.setBackgroundResource(R.drawable.doc_file_ppt);
                break;
            case Constant.ZIP:
                viewHolder.docTypeImage.setBackgroundResource(R.drawable.doc_file_zip);
                break;
        }
        viewHolder.publisher.setText(docDetailBean.getPublisher());
        viewHolder.releasetime.setText(docDetailBean.getReleaseTime());
        viewHolder.isfavorited.setBackgroundResource(docDetailBean.isFavorite() ? R.drawable.doc_ic_favorited : R.drawable.doc_ic_unfavorite);
    }

    public void action(final int position) {
        viewHolder.isfavorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (docDetails.get(position).isFavorite()) {
                    docDetails.get(position).setFavorite(false);
                    refresh();
                } else {
                    docDetails.get(position).setFavorite(true);
                    refresh();
                }

            }
        });
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    private final class ViewHolder {
        public TextView detailDocName;
        public TextView publisher;
        public TextView releasetime;
        public ImageView docTypeImage;
        public ImageView isfavorited;
    }
}
