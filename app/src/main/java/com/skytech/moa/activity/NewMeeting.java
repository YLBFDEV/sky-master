package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.R;
import com.skytech.moa.presenter.NewMeetingPresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewMeetingView;
import org.json.JSONObject;

public class NewMeeting extends Activity implements INewMeetingView {

    private NewMeetingPresenter presenter;
    private FormView form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initView();
        presenter = new NewMeetingPresenter(this);
        presenter.fetchNewMeetingForm();
    }

    private void initView() {
        setContentView(R.layout.new_meeting);
        form = (FormView)findViewById(R.id.new_meeting_form);
    }

    @Override
    public void loadForm(JSONObject data) {
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), null);
        form.setData(data);
    }
}
