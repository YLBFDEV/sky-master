package com.skytech.moa.view;

import org.json.JSONObject;

public interface IApplyExpenseView {
    /**
     * to load form according to data given
     * @param data
     */
    public void loadForm(JSONObject data);
}
