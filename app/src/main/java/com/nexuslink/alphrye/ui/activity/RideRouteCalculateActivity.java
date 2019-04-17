package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.nexuslink.alphrye.cyctastic.R;


public class RideRouteCalculateActivity extends BaseActivity {
    public static final String START_POINT = "start_point";
    public static final String END_POINT = "end_point";
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mStartPoint = intent.getParcelableExtra(START_POINT);
        mEndPoint = intent.getParcelableExtra(END_POINT);
        if (mStartPoint == null
                || mEndPoint == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateRideRoute(new NaviLatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                new NaviLatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        super.onCalculateRouteSuccess(ints);
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
