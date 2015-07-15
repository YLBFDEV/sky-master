package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.R;
import com.skytech.moa.presenter.NewMemoPresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewMemoView;
import org.json.JSONObject;

public class NewMemo extends Activity implements INewMemoView {

    private NewMemoPresenter presenter;
    private FormView form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        initView();
        presenter = new NewMemoPresenter(this);
        presenter.fetchNewMemoForm();
    }

    private void initView() {
        setContentView(R.layout.new_workplan);
        form = (FormView)findViewById(R.id.new_workplan_form);
    }

    @Override
    public void loadForm(JSONObject data) {
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), null);
        form.setData(data);
    }
}
