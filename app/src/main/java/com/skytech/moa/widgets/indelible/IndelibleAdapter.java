package com.skytech.moa.widgets.indelible;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
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
import com.skytech.moa.model.Contact;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * indelible adapter
 */
public class IndelibleAdapter extends BaseAdapter implements SectionIndexer{

    private Context context;
    private List<Contact> contacts;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private DisplayImageOptions options;

    public IndelibleAdapter(Context context) {
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

    public void updateList(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        if (null != contacts) {
            return contacts.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != contacts) {
            return contacts.get(position);
        }
        return new Contact();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
            holder.letter = (TextView) convertView.findViewById(R.id.cata_log);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.job = (TextView) convertView.findViewById(R.id.job);
            holder.department = (TextView) convertView.findViewById(R.id.department);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contacts.get(position);

        /*//根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(contact.getSortLetters());
        } else {
            holder.letter.setVisibility(View.GONE);
        }*/
        String imageUrl = SkyHttpClient.getInstance().getAppBaseUrl() + "/" + String.format(API.GET_FILE, contact.getPhotoId());
        ImageLoader.getInstance().displayImage(imageUrl, holder.photo, options, animateFirstListener);
        //ImageLoader.getInstance().displayImage(App.getInstance().getAppBaseUrl() + "/attachment/file.do?fileName=image1.png", holder.photo, options, animateFirstListener);
        holder.name.setText(contact.getName());
        holder.job.setText(contact.getDuty());
        holder.department.setText(contact.getDepartment());
        return convertView;
    }

    public void remove(int position) {
        if (null != contacts && position < contacts.size())
            contacts.remove(position);
    }

    class ViewHolder {
        public TextView letter;
        public ImageView photo;
        public TextView name;
        public TextView job;
        public TextView department;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return contacts.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = contacts.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
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
