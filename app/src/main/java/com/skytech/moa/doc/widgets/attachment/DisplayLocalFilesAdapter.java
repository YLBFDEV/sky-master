package com.skytech.moa.doc.widgets.attachment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skytech.android.util.FileUtils;
import com.skytech.moa.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayLocalFilesAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> paths;

    private ArrayList<String> selected = new ArrayList<String>();

    public DisplayLocalFilesAdapter(Context context) {
        mContext = context;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    @Override
    public int getCount() {
        if (paths == null) {
            return 0;
        } else {
            return paths.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.local_file_grid_item, null, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.rememberPassword);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (FileUtils.isAudio(paths.get(i))) {
            imageView.setImageResource(R.drawable.ic_audio);
        } else {
            imageView.setImageResource(R.drawable.ic_loading);
            imageLoader.displayImage("file://" + paths.get(i), imageView);
        }

        checkBox.setChecked(selected.indexOf(paths.get(i)) != -1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected.add(paths.get(i));
                } else {
                    selected.remove(paths.get(i));
                }
            }
        });
        return view;
    }
}

