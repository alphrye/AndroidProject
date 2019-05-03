package com.nexuslink.alphrye.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.MyLogUtil;
import com.nexuslink.alphrye.model.RunningDataModel;
import com.nexuslink.alphrye.model.RunningTimeModel;
import com.nexuslink.alphrye.ui.activity.MapActivity;
import com.nexuslink.alphrye.ui.weight.DashboardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewCycleFragment extends MyLazyFragment {

    public static final String TAG = "NewCycleFragment";

    public static final int STATUS_READY = 0;

    public static final int STATUS_RUNNING = 1;

    public static final int STATUS_PAUSE = 2;

    public static final int FLAG_TIME_START = 0;

    public static final int FLAG_TIME_PAUSE = 1;

    public static final int FLAG_TIME_CONTINUE = 2;

    private static final int FLAG_UPDATE_ALTITUDE = 3;

    public static final int POSITION_ALTITUDE = 2;

    public int mCurrentStatus;

    private SimpleAdapter mSimpleAdapter;

    private static final int mDataRaw = 2;

    private List<SimpleModel> modelList;

    private LocationManager mLocationManager;

    private GnssStatus.Callback mGnssStatusCallback;

    @OnClick(R.id.iv_map)
    void onMapOpen(View view) {
        Intent intent = new Intent(getContext(), MapActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_start_or_pause)
    void onStartOrPause(View view) {
        if (mCurrentStatus == STATUS_READY) {
            //开始计时
            mCurrentStatus = STATUS_RUNNING;
            mBtnDone.setVisibility(View.VISIBLE);
            mBtnStartOrPause.setText("暂停");
            if (modelList == null) {
                return;
            }
            int pos = getRunningTimeItemPos();
            Log.d("Test", "onStartOrPause: " + pos);
            if (pos == -1) {
                return;
            }
            mSimpleAdapter.notifyItemChanged(pos, FLAG_TIME_START);
        } else if (mCurrentStatus == STATUS_RUNNING) {
            //暂停
            mCurrentStatus = STATUS_PAUSE;
            mBtnStartOrPause.setText("继续");
            int pos = getRunningTimeItemPos();
            if (pos == -1) {
                return;
            }
            mSimpleAdapter.notifyItemChanged(pos, FLAG_TIME_PAUSE);
        } else if (mCurrentStatus == STATUS_PAUSE) {
            //继续
            mCurrentStatus = STATUS_RUNNING;
            mBtnStartOrPause.setText("暂停");
            int pos = getRunningTimeItemPos();
            if (pos == -1) {
                return;
            }
            mSimpleAdapter.notifyItemChanged(pos, FLAG_TIME_CONTINUE);
        }
    }

    private int getRunningTimeItemPos() {
        if (modelList == null || modelList.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < modelList.size(); i++) {
            //计时器只有一个，遍历到第一个就返回
            SimpleModel model = modelList.get(i);
            if (model == null) {
                continue;
            }
            if (model instanceof RunningTimeModel) {
                return i;
            }
        }
        return -1;
    }

    @OnClick(R.id.btn_done)
    void onDone(View view) {
        mCurrentStatus = STATUS_READY;
        mBtnDone.setVisibility(View.GONE);
        mBtnStartOrPause.setText("开始");
    }

    @BindView(R.id.btn_done)
    TextView mBtnDone;

    @BindView(R.id.btn_start_or_pause)
    TextView mBtnStartOrPause;

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_debug)
    TextView mTvDebug;

    @BindView(R.id.v_dash_board)
    DashboardView mDashboardView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_cycle;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_cycle_title;
    }

    @Override
    protected void initView() {
        mBtnDone.setVisibility(View.GONE);
        mBtnStartOrPause.setText("开始");
        mCurrentStatus = STATUS_READY;

        modelList = new ArrayList<>();

        modelList.add(0, new RunningDataModel("里程(KM)", "-- KM"));
        modelList.add(1, new RunningDataModel("热量(卡路里)", "-- 卡路里"));
        modelList.add(POSITION_ALTITUDE, new RunningDataModel("实时海拔", "-- KM"));

        modelList.add(3, new RunningTimeModel("骑行时间(秒)"));


        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(mRecyclerView)
                .layoutManager(new StaggeredGridLayoutManager(mDataRaw, StaggeredGridLayoutManager.VERTICAL))
                .data(modelList)
                .build();
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssStatusCallback = new GnssStatus.Callback() {
                @Override
                public void onStarted() {
                    super.onStarted();
                    MyLogUtil.d(TAG, "onStarted: ");
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    MyLogUtil.d(TAG, "onStopped: ");
                }

                @Override
                public void onFirstFix(int ttffMillis) {
                    super.onFirstFix(ttffMillis);
                    MyLogUtil.d(TAG, "onFirstFix" );
                }

                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {
                    super.onSatelliteStatusChanged(status);
                    MyLogUtil.d(TAG, "onSatelliteStatusChanged: ");
                }
            };
        } else {
            // TODO: 2019/4/21
        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //打开GPS
        } else {
            String bestProvider = mLocationManager.getBestProvider(getLocationCriteria(), true);
            //权限检查
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLocationManager.registerGnssStatusCallback(mGnssStatusCallback);
            } else {
                // TODO: 2019/4/21
            }
            Location location = mLocationManager.getLastKnownLocation(bestProvider);
            updateAltitudeByLocation(location);
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    return;
                }
                MyLogUtil.d(TAG, "onLocationChanged: location latitude = " + location.getLatitude() + ", longitude = " + location.getLongitude());
                updateSpeedByLocation(location);
                updateAltitudeByLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                MyLogUtil.d(TAG, "onStatusChanged: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                MyLogUtil.d(TAG, "onProviderEnabled: ");
                Location location = mLocationManager.getLastKnownLocation(provider);
                updateSpeedByLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                MyLogUtil.d(TAG, "onProviderDisabled: ");
            }
        };
        // TODO: 2019/4/21 权限申请
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

    }

    /**
     * 根据位置获取海拔信息
     * @param location
     */
    private void updateAltitudeByLocation(Location location) {
        if (location == null) {
            return;
        }
        double altitude = location.getAltitude();
        MyLogUtil.d(TAG, "updateAltitudeByLocation: " + altitude);
//        mTvDebug.setText(mTvDebug.getText() + " altitude: " + altitude);
         SimpleModel model = modelList.get(POSITION_ALTITUDE);
         if (model instanceof  RunningDataModel) {
             ((RunningDataModel) model).mData = altitude + "";
         }
        mSimpleAdapter.notifyItemChanged(POSITION_ALTITUDE, FLAG_UPDATE_ALTITUDE);
    }

    /**
     * 根据位置获取速度
     * @param location
     */
    private void updateSpeedByLocation(Location location) {
        if (location == null) {
            return;
        }
        float speed = location.getSpeed();
        MyLogUtil.d(TAG, "updateSpeedByLocation: speed = " + speed);
        mTvDebug.setText("speed: " + speed);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            float speedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
            MyLogUtil.d(TAG, "updateSpeedByLocation: speedAccuracyMetersPerSecond = " + speedAccuracyMetersPerSecond);
//            mTvDebug.setText(mTvDebug.getText() + " speedAccuracyMetersPerSecond: " + speedAccuracyMetersPerSecond);
        }
        mDashboardView.updateSpeed(speed);
    }

    /**
     * 设置查询条件
     * @return
     */
    private Criteria getLocationCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true); // 设置是否要求速度
        criteria.setCostAllowed(false); // 设置是否允许运营商收费
        criteria.setBearingRequired(false); // 设置是否需要方位信息
        criteria.setAltitudeRequired(true); // 设置是否需要海拔信息
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 设置对电源的需求
        return criteria;
    }

    public static NewCycleFragment newInstance() {
        return new NewCycleFragment();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLocationManager.unregisterGnssStatusCallback(mGnssStatusCallback);
            } else {

            }
        }
    }
}
