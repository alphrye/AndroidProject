package com.nexuslink.alphrye.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.AMapUtil;
import com.nexuslink.alphrye.helper.RideRouteOverlay;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.OnClick;

import static com.nexuslink.alphrye.ui.activity.HomeActivity.REQUEST_SEARCH_TIP;

public class MapActivity extends MyActivity {

    public static final float DEFAULT_ZOOM_LEVEL = 17.0f;

    private MapView mMapView;
    private AMap mAmap;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private RouteSearch mRouteSearch;
    private double mCurLatitude;
    private double mCurLongtitude;
//    private AMapNaviView mAMapNaviView;
//    private AMapNavi mAMapNavi;

    @OnClick(R.id.v_search) void onSearchClick (View v){
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivityForResult(intent,REQUEST_SEARCH_TIP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = findViewById(R.id.v_map);
        mMapView.onCreate(savedInstanceState);

        //Amap（Amap设置）
        mAmap = mMapView.getMap();
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAmap.setMyLocationEnabled(true);
        mAmap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d("Test", "onMyLocationChange: " + location.getAltitude());
            }
        });
        //显示实时路况图层，aMap是地图控制器对象。
        mAmap.setTrafficEnabled(true);
        mAmap.showBuildings(true);
        mAmap.showIndoorMap(true);
        mAmap.showMapText(true);
        mAmap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL));

        //LocationStyle（定位蓝点Style）
        myLocationStyle = new MyLocationStyle();
        // TODO: 2019/4/9 第一次移动到中心，后面的连续定位不会移动到中心
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置精度圆圈填充颜色为透明
        myLocationStyle.radiusFillColor(Color.parseColor("#00000000"));
        //设置描边颜色为透明
        myLocationStyle.strokeColor(Color.parseColor("#00000000"));
        // TODO: 2019/4/8 更新定位图标
//            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.test_avatar));
//            myLocationStyle.anchor(0.0f, 1.0f);
        //设置定位蓝点的Style
        mAmap.setMyLocationStyle(myLocationStyle);

        //地图UI设置
        UiSettings uiSettings = mAmap.getUiSettings();
        if (uiSettings != null) {
            //控件交互
            //设置默认定位按钮是否显示，非必需设置。
            uiSettings.setMyLocationButtonEnabled(false);
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setCompassEnabled(false);
            uiSettings.setScaleControlsEnabled(true);
            uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);

            //手势交互
            uiSettings.setAllGesturesEnabled(true);

        }

        //定位请求
        //定位请求相关配置
        mLocationOption = new AMapLocationClientOption();
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
//            mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //定位模式：Device_Sensors(仅用设备定位模式) Hight_Accuracy(高精度定位模式) Battery_Saving(低功耗定位模式)
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient = new AMapLocationClient(getContext());
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        // TODO: 2019/4/9 避免一直赋值操作
                        mCurLatitude = aMapLocation.getLatitude();
                        mCurLongtitude = aMapLocation.getLongitude();
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getLatitude();//获取纬度
                        aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        aMapLocation.getCountry();//国家信息
                        aMapLocation.getProvince();//省信息
                        aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                        aMapLocation.getFloor();//获取当前室内定位的楼层
                        aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                        //获取定位时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);
                        Log.d("Test", "onLocationChanged: " + aMapLocation.getStreet());
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("Test","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            mLocationClient.stopLocation();
        mLocationClient.startLocation();



//            //导航规划
//            mAMapNaviView = findViewById(R.id.v_map_navi);
//            mAMapNaviView.setAMapNaviViewListener(new AMapNaviViewListener() {
//                @Override
//                public void onNaviSetting() {
//
//                }
//
//                @Override
//                public void onNaviCancel() {
//
//                }
//
//                @Override
//                public boolean onNaviBackClick() {
//                    return false;
//                }
//
//                @Override
//                public void onNaviMapMode(int i) {
//
//                }
//
//                @Override
//                public void onNaviTurnClick() {
//
//                }
//
//                @Override
//                public void onNextRoadClick() {
//
//                }
//
//                @Override
//                public void onScanViewButtonClick() {
//
//                }
//
//                @Override
//                public void onLockMap(boolean b) {
//
//                }
//
//                @Override
//                public void onNaviViewLoaded() {
//
//                }
//            });
//            mAMapNaviView.onCreate(savedInstanceState);
//
//            //骑行路线规划
//            //获取AMapNavi实例
//            mAMapNavi = AMapNavi.getInstance(getContext());
//            //添加监听回调，用于处理算路成功
//

//            09:52:5C:AF:38:CE:5A:F8:FA:A6:CD:6F:EB:5F:3C:F3:50:69:00:20
//            Log.e("Test", "onCreateView: SHA:" + sHA1(getContext()) );

        mRouteSearch = new RouteSearch(getContext());
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult result, int errorCode) {
                Log.d("Test", "onRideRouteSearched: ");
                mAmap.clear();// 清理地图上的所有覆盖物
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            final RidePath ridePath = result.getPaths()
                                    .get(0);
                            if(ridePath == null) {
                                return;
                            }
                            RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                                    getContext(), mAmap, ridePath,
                                    result.getStartPos(),
                                    result.getTargetPos());
                            rideRouteOverlay.removeFromMap();
                            rideRouteOverlay.addToMap();
                            rideRouteOverlay.zoomToSpan();
//                                mBottomLayout.setVisibility(View.VISIBLE);
                            int dis = (int) ridePath.getDistance();
                            int dur = (int) ridePath.getDuration();
                            String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//                                mRotueTimeDes.setText(des);
//                                mRouteDetailDes.setVisibility(View.GONE);
//                                mBottomLayout.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(mContext,
//                                                RideRouteDetailActivity.class);
//                                        intent.putExtra("ride_path", ridePath);
//                                        intent.putExtra("ride_result",
//                                                mRideRouteResult);
//                                        startActivity(intent);
//                                    }
//                                });

                            Intent intent = new Intent(getContext(), RideRouteCalculateActivity.class);
                            intent.putExtra("start_point", result.getStartPos());
                            intent.putExtra("end_point", result.getTargetPos());
                            startActivity(intent);

                        } else if (result != null && result.getPaths() == null) {
//                                ToastUtil.show(mContext, R.string.no_result);
                        }
                    } else {
//                            ToastUtil.show(mContext, R.string.no_result);
                    }
                } else {
//                        ToastUtil.showerror(this.getApplicationContext(), errorCode);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cycle;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_cycle_title;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
//        mAMapNaviView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
//        mAMapNaviView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
//        mAMapNaviView.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务。
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SEARCH_TIP) {
                if (data == null) {
                    return;
                }
                Tip tip = data.getParcelableExtra("search_tip");
                if (tip == null) {
                    return;
                }
                Log.d("Test", "onActivityResult: " + tip.getName());
                final LatLonPoint point = tip.getPoint();
                if (point == null) {
                    return;
                }

                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(getCurrentPoint(), point);

                RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo);
                mRouteSearch.calculateRideRouteAsyn(query);

//                mAMapNavi.addAMapNaviListener(new AMapNaviListener() {
//                    @Override
//                    public void onInitNaviFailure() {
//                        Log.d("Test", "onInitNaviFailure: ");
//                    }
//
//                    @Override
//                    public void onInitNaviSuccess() {
//                        mAMapNavi.calculateRideRoute(new NaviLatLng(point.getLatitude(), point.getLongitude()));
////                        mAMapNavi.calculateRideRoute(new NaviLatLng(39.92, 116.43), new NaviLatLng(39.92, 116.53));
//                    }
//
//                    @Override
//                    public void onStartNavi(int i) {
//                        Log.d("Test", "onStartNavi: ");
//                    }
//
//                    @Override
//                    public void onTrafficStatusUpdate() {
//
//                    }
//
//                    @Override
//                    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
//
//                    }
//
//                    @Override
//                    public void onGetNavigationText(int i, String s) {
//
//                    }
//
//                    @Override
//                    public void onEndEmulatorNavi() {
//
//                    }
//
//                    @Override
//                    public void onArriveDestination() {
//
//                    }
//
//                    @Override
//                    public void onCalculateRouteSuccess() {
//                        mAMapNavi.startNavi(NaviType.GPS);
//                    }
//
//                    @Override
//                    public void onCalculateRouteFailure(int i) {
//
//                    }
//
//                    @Override
//                    public void onReCalculateRouteForYaw() {
//
//                    }
//
//                    @Override
//                    public void onReCalculateRouteForTrafficJam() {
//
//                    }
//
//                    @Override
//                    public void onArrivedWayPoint(int i) {
//
//                    }
//
//                    @Override
//                    public void onGpsOpenStatus(boolean b) {
//
//                    }
//
//                    @Override
//                    public void onNaviInfoUpdate(NaviInfo naviInfo) {
//
//                    }
//
//                    @Override
//                    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
//
//                    }
//
//                    @Override
//                    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
//
//                    }
//
//                    @Override
//                    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
//
//                    }
//
//                    @Override
//                    public void showCross(AMapNaviCross aMapNaviCross) {
//
//                    }
//
//                    @Override
//                    public void hideCross() {
//
//                    }
//
//                    @Override
//                    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
//
//                    }
//
//                    @Override
//                    public void hideLaneInfo() {
//
//                    }
//
//                    @Override
//                    public void onCalculateMultipleRoutesSuccess(int[] ints) {
//
//                    }
//
//                    @Override
//                    public void notifyParallelRoad(int i) {
//
//                    }
//
//                    @Override
//                    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
//
//                    }
//
//                    @Override
//                    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//
//                    }
//
//                    @Override
//                    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
//
//                    }
//
//                    @Override
//                    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
//
//                    }
//
//                    @Override
//                    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
//
//                    }
//
//                    @Override
//                    public void onPlayRing(int i) {
//
//                    }
//                });
            }
        }
    }

    private LatLonPoint getCurrentPoint() {
        return new LatLonPoint(mCurLatitude, mCurLongtitude);
    }
}
