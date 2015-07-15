package com.skytech.moa.doc.widgets.attachment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.android.Logging;
import com.skytech.android.util.BitmapUtils;
import com.skytech.android.util.FileUtils;
import com.skytech.moa.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttachmentAdapter extends BaseAdapter {
    private List<AttachmentEntity> attachmentEntities = new ArrayList<AttachmentEntity>();
    private Context mContext;
    private boolean isEditable;

    public AttachmentAdapter(Context context) {
        mContext = context;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setFiles(List<AttachmentEntity> files) {
        this.attachmentEntities = files;
        notifyDataSetChanged();
    }

    public List getFiles() {
        return attachmentEntities;
    }

    private void addFile(AttachmentEntity file) {
        attachmentEntities.add(file);
        notifyDataSetChanged();
    }

    public void addFile(String path) {
        for (Iterator i = attachmentEntities.iterator(); i.hasNext(); ) {
            AttachmentEntity attachmentEntity = (AttachmentEntity) i.next();
            if (attachmentEntity.getFilePath().equals(path))
                return;
        }
        addFile(new AttachmentEntity(path,
                false));
    }

    public void addFiles(List paths) {
        for (int i = 0; i < paths.size(); i++) {
            addFile((String) paths.get(i));
        }
    }

    @Override
    public int getCount() {
        if (isEditable) {
            return attachmentEntities.size() + 1;
        } else {
            return attachmentEntities.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return attachmentEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from
                    (mContext).inflate(R.layout.attatchment_grid_item, null, false);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.speed = (TextView) view.findViewById(R.id.speed);
            holder.selected = false;
            holder.deleteImage = (ImageView) view.findViewById(R.id.deleteImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (isEditable && i == getCount() - 1) {
            showPlusView(holder);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.back_button_selector);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.deleteImage.setVisibility(isEditable ? View.VISIBLE : View.GONE);

            Bitmap bitmap = getBitmapByFilePath(attachmentEntities.get(i).getFilePath());
            holder.imageView.setImageBitmap(BitmapUtils.getBitmapByDefaultRes(bitmap, R.drawable.img_add, mContext));
            //可编辑的情况下可以删除图片
            holder.deleteImage.setOnClickListener(deleteFile(i));
        }
        return view;
    }

    private Bitmap getBitmapByFilePath(String path) {
        Bitmap bitmap;
        if (null != path) {
            if (path.equals("file.no")) {//附件未找到
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_load_error);
            } else if (path.equals("error")) {//附件下载失败
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_load_error);
            } else {
                //如果是图片，则显示该图片缩放以后的图片；如果是录音，则显示“ic_audio”
                if (FileUtils.isImage(path)) {
                    try {
                        bitmap = BitmapUtils.decodeSampledBitmapFromSD(path, 100, 100);
                    } catch (OutOfMemoryError error) {
                        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_load_error);
                        Log.e(Logging.LOG_TAG, "image too big lead to oom");
                    }
                } else if (FileUtils.isAudio(path)) {
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_audio);
                } else {//文件格式未识别
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_load_error);
                }
            }
        } else {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_loading);
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_load_error);
        }
        return bitmap;
    }

    private void showPlusView(ViewHolder holder) {
        holder.imageView.setImageBitmap(BitmapUtils.readBitMap(mContext, R.drawable.img_add));
        holder.deleteImage.setVisibility(View.GONE);
        holder.imageView.setBackgroundResource(R.drawable.back_button_selector);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    private View.OnClickListener deleteFile(final int num) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentEntities.remove(num);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder {
        boolean selected;
        ImageView imageView;
        TextView speed;
        ImageView deleteImage;
    }
}
