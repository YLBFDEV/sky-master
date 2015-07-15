package com.skytech.moa.manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skytech.android.Logging;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.API;
import com.skytech.moa.services.BaseService;
import com.skytech.moa.utils.Constant;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceManager extends BaseService implements BDLocationListener, OnGetGeoCoderResultListener {
    private final String TAG = "AttendanceManager";
    public interface LocationListener {
        public void onReceiveLocation(BDLocation location);

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result);
    }

    // 定位相关
    private LocationClient mLocClient;
    private GeoCoder mSearch;
    private LocationListener dlg;

    public AttendanceManager(Context context, LocationListener delegated, Handler handler) {
        super(handler);
        httpClient = SkyHttpClient.getInstance();
        dlg = delegated;
        // 定位初始化
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3600);
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;
        LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        dlg.onReceiveLocation(location);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.i(Logging.LOG_TAG, "抱歉，未能找到结果");
            return;
        }
        dlg.onGetReverseGeoCodeResult(result);
    }

    public void signIn(int type, double latitude, double longitude, String address) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constant.PARAM_TYPE, type);
            params.put(Constant.PARAM_LATITUDE, latitude);
            params.put(Constant.PARAM_LONGITUDE, longitude);
            params.put(Constant.PARAM_ADDRESS, address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpClient.post(API.ATTENDANCE_SIGNIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optBoolean(Constant.PARAM_SUCCESS)) {
                    sendMsg(Constant.DETAIL_SUCCESS);
                } else {
                    error = response.optString(Constant.PARAM_MESSAGE);
                    sendMsg(Constant.DETAIL_FAILURE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                sendMsg(Constant.LIST_FAILURE);
                Log.e(TAG, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
