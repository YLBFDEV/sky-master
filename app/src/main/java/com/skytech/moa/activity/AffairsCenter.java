package com.skytech.moa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.skytech.android.adapter.ArkAdapter;
import com.skytech.moa.R;

import java.util.ArrayList;
import java.util.List;

public class AffairsCenter extends Activity {

    private ListView affairsEntry;
    private ArkAdapter affairsAdapter = new ArkAdapter();
    private List<AffairInfo> affairInfos = new ArrayList<AffairInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }


    private void initView() {
        setContentView(R.layout.affairs_center);
        affairsEntry = (ListView) findViewById(R.id.affairs_entry);
        affairsAdapter.setAdapterHandler(affairsEntryAdapterHandler);
        affairsEntry.setAdapter(affairsAdapter);
        affairsEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AffairsCenter.this, affairInfos.get(position).destination);
                startActivity(intent);
            }
        });
    }


    private ArkAdapter.AdapterHandler affairsEntryAdapterHandler = new ArkAdapter.AdapterHandler() {
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AffairsCenter.this).inflate(R.layout.item_affair_entry, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.description = (TextView) convertView.findViewById(R.id.description);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.icon.setImageResource(affairInfos.get(position).icon);
            holder.title.setText(affairInfos.get(position).title);
            holder.description.setText(affairInfos.get(position).description);

            return convertView;
        }

        @Override
        public int getCount() {
            return affairInfos.size();
        }
    };


    private void initData() {
        affairInfos.clear();
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_apply_affair), getString(R.string.desc_affair_apply_affair), R.drawable.icon_affair_apply_affair, ApplyAffair.class));
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_meeting_management), getString(R.string.desc_affair_meeting_management), R.drawable.icon_affair_meeting, ApplyAffair.class));
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_memo), getString(R.string.desc_affair_memo), R.drawable.icon_affair_memo, ApplyAffair.class));
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_task), getString(R.string.desc_affair_task), R.drawable.icon_affair_task, ApplyAffair.class));
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_work_log), getString(R.string.desc_affair_work_log), R.drawable.icon_affair_work_log, NewWorkLogs.class));
        affairInfos.add(new AffairInfo(getString(R.string.title_affair_fee), getString(R.string.desc_affair_fee), R.drawable.icon_affair_fee, ApplyExpense.class));
    }


    private final class AffairInfo {
        public String title;
        public String description;
        public int icon;
        public Class<?> destination;

        public AffairInfo(String title, String description, int icon, Class<?> destination) {
            this.title = title;
            this.icon = icon;
            this.description = description;
            this.destination = destination;
        }
    }


    private final class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView description;
    }

}
