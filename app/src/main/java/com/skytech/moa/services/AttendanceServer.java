package com.skytech.moa.services;

import android.os.Handler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.util.DateUtils;
import com.skytech.moa.API;
import com.skytech.moa.App;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AttendanceServer extends BaseService {
    public AttendanceServer(Handler handler) {
        super(handler);
    }

    private JSONArray array;

    public JSONArray getAttendances() {
        return array;
    }

    public void list(Date sdata, Date edata) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constant.PARAM_USERID, App.getInstance().getUser().getId());
            params.put(Constant.PARAMETER_STARTTIME, DateUtils.formatDate(sdata));
            params.put(Constant.PARAMETER_ENDTIME, DateUtils.formatDate(edata));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpClient.get(API.ATTENDANCE_LIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    array = response.optJSONArray(Constant.NOTICES);
                    sendMsg(Constant.LIST_SUCCESS);
                } else {
                    error = response.optString(Constant.PARAM_MESSAGE);
                    sendMsg(Constant.LIST_FAILURE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                sendMsg(Constant.LIST_FAILURE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.LIST_FAILURE);
            }
        });
    }
}
