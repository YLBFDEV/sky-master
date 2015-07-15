package com.skytech.moa.activity;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.skytech.android.util.CustomToast;
import com.skytech.android.util.FileUtils;
import com.skytech.android.util.PathUtil;
import com.skytech.moa.R;
import com.skytech.moa.adapter.NoticeAttachmentAdapter;
import com.skytech.moa.manager.DocDetailModel;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.utils.PopupDialog;
import com.skytech.moa.widgets.doclibrary.DocDetailAdapter;

import java.io.File;

public class DocDetailItemListener implements AdapterView.OnItemClickListener, DocDetailModel.Callback {
    private DocDetailAdapter docDetailAdapter;
    private NoticeAttachmentAdapter noticeAttachmentAdapter;

    private PopupDialog downloadDialog;
    private ProgressBar progressBar;
    private Activity activity;
    private String fileName;
    private ClickHappend callback;

    @Override
    public void downloadFileSuccess(String filePath) {
        activity.startActivityForResult(FileUtils.openFile(filePath), Constant.REQUEST_ATTACHMENT);
        downloadDialog.dismiss();
    }

    @Override
    public void downloadFileFailure(String msg) {
        downloadDialog.dismiss();
        CustomToast.show(activity, msg);
    }

    @Override
    public void downloadFileProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public interface ClickHappend {
        public void downloadAttachment(String fileName);
    }

    public DocDetailItemListener(DocDetailAdapter docDetailAdapter, Activity activity, ClickHappend callback) {
        this.docDetailAdapter = docDetailAdapter;
        this.activity = activity;
        this.callback = callback;
    }

    public DocDetailItemListener(NoticeAttachmentAdapter noticeAttachmentAdapter, Activity activity, ClickHappend callback) {
        this.noticeAttachmentAdapter = noticeAttachmentAdapter;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        DocumentDetail docDetail = (DocumentDetail) docDetailAdapter.getItem(i - 1);
        DocumentDetail docDetail = (DocumentDetail) noticeAttachmentAdapter.getItem(i);
        System.out.println(docDetail.getExtension());
        File destDir = new File(PathUtil.getInstance().getAttachmentDir());
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String extension = docDetail.getExtension();
        loadAttachment(docDetail, extension);

    }

    private void loadAttachment(DocumentDetail docDetail, String extension) {

        if (null != downloadDialog && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
        }
        // 下载附件
        downloadDialog = new PopupDialog(activity,
                400, LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.download_attachment, R.style.Theme_dialog);
        downloadDialog.show();
        downloadDialog.findViewById(R.id.btnCancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadDialog.dismiss();
                    }
                }
        );
        progressBar = (ProgressBar) downloadDialog.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        fileName = docDetail.getName().replaceAll("　", "-").replaceAll(" ", "-") + "." + extension;
        callback.downloadAttachment(fileName);
    }
}
