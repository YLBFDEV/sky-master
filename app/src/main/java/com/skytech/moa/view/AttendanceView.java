package com.skytech.moa.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.skytech.android.util.CustomToast;
import com.skytech.android.util.StringUtils;
import com.skytech.moa.R;
import com.skytech.moa.manager.AttendanceManager;
import com.skytech.moa.utils.Constant;


public class AttendanceView implements AttendanceManager.LocationListener, View.OnClickListener {
    public interface OnClickListener {
        public void onClickSignIn(int type, double latitude, double longitude, String address);

        public void onClickRecord();
    }

    private Activity activity;
    private OnClickListener dlg;

    private MapView mMapView;
    private TextView address;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;// 是否首次定位
    private BDLocation location;

    public AttendanceView(Activity act, OnClickListener delegated) {
        activity = act;
        dlg = delegated;
        address = (TextView) act.findViewById(R.id.text_address);
        // 地图初始化
        mMapView = (MapView) act.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        ((TextView) act.findViewById(R.id.title)).setText("考勤签到");
        act.findViewById(R.id.up_work).setOnClickListener(this);
        act.findViewById(R.id.dow_work).setOnClickListener(this);
        act.findViewById(R.id.button_record).setOnClickListener(this);

        //设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        //mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        //设置是否显示比例尺控
        mMapView.showScaleControl(false);
    }

    public void onPause() {
        mMapView.onPause();
    }

    public void onResume() {
        mMapView.onResume();
    }

    public void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    /**
     * 定位SDK监听函数
     *
     * @param location
     */
    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null)
            return;
        this.location = location;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        address.setText(result.getAddress());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_work:
                if (check())
                    dlg.onClickSignIn(Constant.ATTENDANCE_TYPE_UP_WORK, location.getLatitude(), location.getLongitude(), address.getText().toString());
                break;
            case R.id.dow_work:
                if (check())
                    dlg.onClickSignIn(Constant.ATTENDANCE_TYPE_DOW_WORK, location.getLatitude(), location.getLongitude(), address.getText().toString());
                break;
            case R.id.button_record:
                dlg.onClickRecord();
                break;
        }
    }

    private boolean check() {
        if (null == location || StringUtils.isEmpty(address.getText().toString())) {
            CustomToast.show(activity, "请检查GPS、网络状态，然后再试");
            return false;
        }
        return true;
    }
}
