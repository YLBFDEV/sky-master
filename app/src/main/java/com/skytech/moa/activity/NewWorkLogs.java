package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.R;
import com.skytech.moa.presenter.NewWorkLogsPresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewWorkLogsView;
import org.json.JSONObject;

public class NewWorkLogs extends Activity implements INewWorkLogsView {

    private NewWorkLogsPresenter presenter;
    private FormView form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        initView();
        presenter = new NewWorkLogsPresenter(this);
        presenter.fetchNewWorkLogsForm();
    }

    private void initView() {
        setContentView(R.layout.new_worklogs);
        form = (FormView)findViewById(R.id.new_worklogs_form);
    }

    @Override
    public void loadForm(JSONObject data) {
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), null);
        form.setData(data);
    }
}
