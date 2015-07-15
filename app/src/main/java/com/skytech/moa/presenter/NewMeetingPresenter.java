package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.moa.services.AffairService;
import com.skytech.moa.services.IAffairService;
import com.skytech.moa.services.INewMeetingService;
import com.skytech.moa.services.NewMeetingService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.IApplyAffairView;
import com.skytech.moa.view.INewMeetingView;

public class NewMeetingPresenter {
    private INewMeetingView view;
    private INewMeetingService service;

    private NewMeetingPresenter() {
        service = new NewMeetingService();
    }

    public NewMeetingPresenter(INewMeetingView view) {
        this();
        this.view = view;
    }

    public void fetchNewMeetingForm() {
        RequestParams params = new RequestParams();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.AFFAIR_TYPE, "");
    }
}
