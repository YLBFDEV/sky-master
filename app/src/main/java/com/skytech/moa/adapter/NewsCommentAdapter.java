package com.skytech.moa.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.API;
import com.skytech.moa.R;
import com.skytech.moa.model.NewsCommentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/3.
 */
public class NewsCommentAdapter extends BaseAdapter {

    private final static String TAG = NewsCommentAdapter.class.getSimpleName();

    private Context context;

    private ArrayList<NewsCommentInfo> newsCommentInfos;
    private NewsCommentInfo newsCommentInfo;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsCommentAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_head)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.image_error)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    public void setNewsCommentInfos(ArrayList<NewsCommentInfo> list) {
        this.newsCommentInfos = list;
    }

    @Override
    public int getCount() {
        if (null == newsCommentInfos) {
            return 0;
        }
        Log.i(TAG, "............" + newsCommentInfos.size());
        return newsCommentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return newsCommentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_comment_item, null);
            holder = new ViewHolder();
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_news_comment_item_uidpic);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_news_comment_item_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_news_comment_item_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_news_comment_item_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        newsCommentInfo = newsCommentInfos.get(position);

        String imageUrl = SkyHttpClient.getInstance().getAppBaseUrl() + "/" + String.format(API.GET_FILE, newsCommentInfo.getUidPic());
        ImageLoader.getInstance().displayImage(imageUrl, holder.ivPic, options, animateFirstListener);

        holder.tvName.setText(newsCommentInfo.getName());
        holder.tvTime.setText(newsCommentInfo.getTime());
        holder.tvContent.setText(newsCommentInfo.getContent());
        return convertView;
    }

    private class ViewHolder{
        private ImageView ivPic;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvContent;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
