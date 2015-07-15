package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.skytech.moa.R;
import com.skytech.moa.adapter.NoticeAttachmentAdapter;
import com.skytech.moa.manager.DocDetailModel;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.services.NoticeAttachmentService;
import com.skytech.moa.view.INoticesListView;

import java.util.ArrayList;

public class NoticeAttachmentActivity extends Activity implements INoticesListView, View.OnClickListener, DocDetailItemListener.ClickHappend {

    private final static String TAG = NoticeAttachmentActivity.class.getSimpleName();

    private ImageView ivPublicBack;
    private TextView tvPublicTitle;
    private ListView lvNoticeAttachment;

    private NoticeAttachmentService noticeAttachmentService;
    private NoticeAttachmentAdapter noticeAttachmentAdapter;
    private DocDetailItemListener docDetailItemListener;
    private DocDetailModel docDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_attachment_list);
        initView();
        initData();

    }

    private void initView() {
        ivPublicBack = (ImageView) findViewById(R.id.back);
        ivPublicBack.setOnClickListener(this);
        tvPublicTitle = (TextView) findViewById(R.id.title);
        tvPublicTitle.setText("附件列表");
        lvNoticeAttachment = (ListView) findViewById(R.id.lv_notice_attachment);
    }

    private void initData() {
        String id = getIntent().getExtras().getString("nid");
        noticeAttachmentAdapter = new NoticeAttachmentAdapter(this);
        noticeAttachmentService = new NoticeAttachmentService(this);
        //noticeAttachmentService.getNoticeAttachment(id);
        //请求noticeid = 1的附件
        noticeAttachmentService.getNoticeAttachment("1");
        lvNoticeAttachment.setAdapter(noticeAttachmentAdapter);
        docDetailItemListener = new DocDetailItemListener(noticeAttachmentAdapter, this, this);
        lvNoticeAttachment.setOnItemClickListener(docDetailItemListener);
        docDetailModel = new DocDetailModel(docDetailItemListener);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void loadNoticeAttachmentSuccess(ArrayList<DocumentDetail> list) {
        noticeAttachmentAdapter.setNoticeAttachmentAdapter(list);
    }

    @Override
    public void loadNoticeAttachmentFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT);
    }

    @Override
    public void downloadAttachment(String fileName) {
        docDetailModel.downloadFile(fileName);
    }
}
