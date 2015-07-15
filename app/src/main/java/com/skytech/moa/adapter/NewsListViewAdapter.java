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
import com.skytech.moa.manager.NewsManager;
import com.skytech.moa.model.NewsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class NewsListViewAdapter extends BaseAdapter {
    private final static String TAG = NewsListViewAdapter.class.getSimpleName();

    private Context context;

    private ArrayList<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
    private NewsInfo newsInfo;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsListViewAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_head)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_load_error)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    /**
     * 接收数据 用于展示请求下来的数据
     * @param
     */
    public void setNewsInfos(ArrayList<NewsInfo> list) {
        this.newsInfos = list;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "........................" + newsInfos.size());
        if (null == newsInfos) {
            return 0;
        }
        return newsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return newsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "................................getView()");

        ViewHolder holder = null;

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
            holder = new ViewHolder();
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_news_item_pic);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_item_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_news_item_time);
            holder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tv_news_item_praise);
            holder.ivIsRead = (ImageView) convertView.findViewById(R.id.iv_news_item_isread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        newsInfo = newsInfos.get(position);

        String imageUrl = SkyHttpClient.getInstance().getAppBaseUrl() + "/" + String.format(API.GET_FILE, newsInfo.getPicUrl());
        ImageLoader.getInstance().displayImage(imageUrl, holder.ivPic, options, animateFirstListener);

        holder.tvTitle.setText(newsInfo.getTitle());
        holder.tvTime.setText(newsInfo.getTime());
        holder.tvPraiseNum.setText(String.valueOf(newsInfo.getPraiseNum()));

        if (NewsManager.getInstance().isRead(newsInfo.getId())) {
            holder.ivIsRead.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivPic;
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvPraiseNum;
        private ImageView ivIsRead;
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
