package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.moa.services.INewMeetingService;
import com.skytech.moa.services.INewWorkplanService;
import com.skytech.moa.services.NewWorkplanService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewWorkplanView;

public class NewWorkplanPresenter {
    private INewWorkplanView view;
    private INewWorkplanService service;

    private NewWorkplanPresenter() {
        service = new NewWorkplanService();
    }

    public NewWorkplanPresenter(INewWorkplanView view) {
        this();
        this.view = view;
    }

    public void fetchNewWorkplanForm() {
        RequestParams params = new RequestParams();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.AFFAIR_TYPE, "");
    }
}
