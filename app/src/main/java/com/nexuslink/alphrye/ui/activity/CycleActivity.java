package com.nexuslink.alphrye.ui.activity;

import android.location.Location;
import android.os.Bundle;

import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.LocationHelper;

public class CycleActivity extends MyActivity implements LocationHelper.ILocationChangeObserver {
    private LocationHelper mLocationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cycle;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mLocationHelper = LocationHelper.getInstance();
        mLocationHelper.addObservers(this);
        mLocationHelper.start();
    }

    @Override
    public void onLocationChange(Location location) {
        if (location == null) {
            return;
        }
        toast(location.getSpeed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationHelper == null) {
            return;
        }
        mLocationHelper.removeObservers(this);
    }
}
