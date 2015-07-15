package com.skytech.moa.view;

import com.skytech.moa.model.DocumentDetail;

import java.util.ArrayList;

public interface INoticesListView {

    void loadNoticeAttachmentSuccess(ArrayList<DocumentDetail> list);

    void loadNoticeAttachmentFailure(String error);
}
