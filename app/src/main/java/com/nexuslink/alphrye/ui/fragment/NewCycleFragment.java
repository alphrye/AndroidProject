package com.nexuslink.alphrye.ui.fragment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.api.EagleApiService;
import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.MyLogUtil;
import com.nexuslink.alphrye.helper.SimpleOnTrackLifecycleListener;
import com.nexuslink.alphrye.helper.SimpleOnTrackListener;
import com.nexuslink.alphrye.model.CommonEagleNetModel;
import com.nexuslink.alphrye.model.FullServerModel;
import com.nexuslink.alphrye.model.RunningDataModel;
import com.nexuslink.alphrye.model.RunningTimeModel;
import com.nexuslink.alphrye.model.ServerListModel;
import com.nexuslink.alphrye.model.SimpleServerModel;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;
import com.nexuslink.alphrye.ui.activity.HomeActivity;
import com.nexuslink.alphrye.ui.activity.MapActivity;
import com.nexuslink.alphrye.ui.weight.DashboardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NewCycleFragment extends MyLazyFragment {

    public static final String TAG = "NewCycleFragment";

    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

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

    private AMapTrackClient mAMapTrackClient;

    private long terminalId;

    private long trackId;

    private long serverId;

    private boolean isServiceRunning;

    private boolean isGatherRunning;

    /**
     * 开关
     */
    private boolean uploadToTrack = false;


    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
                Toast.makeText(getContext(), "启动服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
//                updateBtnStatus();
                if (!isGatherRunning) {
                    mAMapTrackClient.setTrackId(trackId);
                    mAMapTrackClient.startGather(onTrackListener);
                }
            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                Toast.makeText(getContext(), "服务已经启动", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;

                if (!isGatherRunning) {
                    mAMapTrackClient.setTrackId(trackId);
                    mAMapTrackClient.startGather(onTrackListener);
                }
//                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStartTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(getContext(),
                        "error onStartTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                Toast.makeText(getContext(), "停止服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = false;
                isGatherRunning = false;
//                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(getContext(),
                        "error onStopTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                Toast.makeText(getContext(), "定位采集开启成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
//                updateBtnStatus();
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                Toast.makeText(getContext(), "定位采集已经开启", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
//                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(getContext(),
                        "error onStartGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                Toast.makeText(getContext(), "定位采集停止成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = false;
//                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(getContext(),
                        "error onStopGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }
    };

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

            //本地检查服务是否开启
            if (!isServiceRunning) {
                //开启猎鹰轨迹上报
                startEagleReport();
            }
        } else if (mCurrentStatus == STATUS_RUNNING) {
            //暂停
            mCurrentStatus = STATUS_PAUSE;
            mBtnStartOrPause.setText("继续");
            int pos = getRunningTimeItemPos();
            if (pos == -1) {
                return;
            }
            mSimpleAdapter.notifyItemChanged(pos, FLAG_TIME_PAUSE);

            //检查值得合法性：isServiceRunning应该为true，isGatherRunning应该为true
            if (!isServiceRunning
                    || !isGatherRunning) {
                // TODO: 2019/5/6 恢复成开始状态
                isGatherRunning = false;
                isServiceRunning = false;
                return;
            }
            //停止采集
            mAMapTrackClient.stopGather(onTrackListener);
        } else if (mCurrentStatus == STATUS_PAUSE) {
            //继续
            mCurrentStatus = STATUS_RUNNING;
            mBtnStartOrPause.setText("暂停");
            int pos = getRunningTimeItemPos();
            if (pos == -1) {
                return;
            }
            mSimpleAdapter.notifyItemChanged(pos, FLAG_TIME_CONTINUE);

            //检查值得合法性：isServiceRunning应该为true，isGatherRunning应该为false
            if (!isServiceRunning || isGatherRunning) {
                // TODO: 2019/5/6 恢复成开始状态
                isGatherRunning = false;
                isServiceRunning = false;
                return;
            }
            //继续开始收集(根据trackId判断是否为同一记录)
            mAMapTrackClient.setTrackId(trackId);
            mAMapTrackClient.startGather(onTrackListener);
        }
    }

    /**
     * 开启猎鹰归集上报服务
     */
    private void startEagleReport() {
        //远端检查服务是否开启
        EagleApiService service = getEagleCallService();
        service.listService(CommonConstance.KEY_EAGLE).enqueue(new Callback<CommonEagleNetModel<ServerListModel>>() {
            @Override
            public void onResponse(Call<CommonEagleNetModel<ServerListModel>> call, Response<CommonEagleNetModel<ServerListModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                CommonEagleNetModel<ServerListModel> model = response.body();
                if (model == null) {
                    return;
                }
                ServerListModel data = model.data;
                if (data == null) {
                    return;
                }
                List<FullServerModel> results = data.results;
                if (results == null
                        || results.isEmpty()) {
                    return;
                }
                String userId = getUserId();
                if (TextUtils.isEmpty(userId)) {
                    toast("user_id 为空");
                    return;
                }
//                boolean hasService = false;
                FullServerModel targetServerModel = null;
                String serviceName = CommonConstance.SERVICE_HEAD+ userId;
                for (FullServerModel serverModel : results) {
                    if (serviceName.equals(serverModel.name)) {
                        targetServerModel = serverModel;
                        break;
                    }
                }
                //targetServerModel不为空表示找到创建的Service
                if (targetServerModel != null) {
                    //service已经创建
                    serverId = targetServerModel.sid;
                    startTrack(serverId);
                } else {
                    //Service没有创建
                    createEagleService();
                }
            }

            @Override
            public void onFailure(Call<CommonEagleNetModel<ServerListModel>> call, Throwable t) {
                toast(t.toString());
            }
        });
    }

    /**
     * 创建猎鹰服务
     */
    private void createEagleService() {
        String userId = getUserId();
        if (TextUtils.isEmpty(userId)) {
            toast("user_id 为空");
            return;
        }
        String name = CommonConstance.SERVICE_HEAD + userId;
        String des = "record_ride";
        EagleApiService service = getEagleCallService();
        service.addService(CommonConstance.KEY_EAGLE, name, des)
                .enqueue(new Callback<CommonEagleNetModel<SimpleServerModel>>() {
                    @Override
                    public void onResponse(Call<CommonEagleNetModel<SimpleServerModel>> call, Response<CommonEagleNetModel<SimpleServerModel>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        CommonEagleNetModel<SimpleServerModel>  model = response.body();
                        if (model == null) {
                            return;
                        }

                        SimpleServerModel data = model.data;
                        if (data == null) {
                            return;
                        }
                        serverId = data.sid;
                        startTrack(serverId);
                    }

                    @Override
                    public void onFailure(Call<CommonEagleNetModel<SimpleServerModel>> call, Throwable t) {
                        toast(t.toString());
                    }
                });
    }

    private String getUserId() {
        return "user_1";
    }

    /**
     * 获取猎鹰请求服务
     * @return
     */
    private EagleApiService getEagleCallService() {
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance(CommonConstance.EAGLE_URL);
        return wrapper.getEagleCall();
    }

    private void startTrack(final long serverId) {
        // 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
        // 用Terminal ID开启轨迹服务
        mAMapTrackClient.queryTerminal(new QueryTerminalRequest(serverId, CommonConstance.TERMINAL_HEAD), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        // 当前终端已经创建过，直接使用查询到的terminal id
                        terminalId = queryTerminalResponse.getTid();
                        if (uploadToTrack) {
                            mAMapTrackClient.addTrack(new AddTrackRequest(serverId, terminalId), new SimpleOnTrackListener() {
                                @Override
                                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                                    if (addTrackResponse.isSuccess()) {
                                        // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
                                        trackId = addTrackResponse.getTrid();
                                        TrackParam trackParam = new TrackParam(serverId, terminalId);
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            trackParam.setNotification(createNotification());
                                        }
                                        mAMapTrackClient.startTrack(trackParam, onTrackListener);
                                    } else {
                                        Toast.makeText(getContext(), "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // 不指定track id，上报的轨迹点是该终端的散点轨迹
                            TrackParam trackParam = new TrackParam(serverId, terminalId);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                trackParam.setNotification(createNotification());
                            }
                            mAMapTrackClient.startTrack(trackParam, onTrackListener);
                        }
                    } else {
                        // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
                        mAMapTrackClient.addTerminal(new AddTerminalRequest(CommonConstance.TERMINAL_HEAD, serverId), new SimpleOnTrackListener() {
                            @Override
                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                                if (addTerminalResponse.isSuccess()) {
                                    terminalId = addTerminalResponse.getTid();
                                    TrackParam trackParam = new TrackParam(serverId, terminalId);
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        trackParam.setNotification(createNotification());
                                    }
                                    mAMapTrackClient.startTrack(trackParam, onTrackListener);
                                } else {
                                    Toast.makeText(getContext(), "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        //检查值得合法性：isServiceRunning应该为true，isServiceRunning应该为true
        if (!isServiceRunning || !isGatherRunning) {
            // TODO: 2019/5/6 恢复成开始状态
            isGatherRunning = false;
            isServiceRunning = false;
            return;
        }

        mAMapTrackClient.stopGather(onTrackListener);
        if (serverId != 0L && terminalId != 0) {
            mAMapTrackClient.stopTrack(new TrackParam(serverId, terminalId), onTrackListener);
        }
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

        modelList.add(0, new RunningDataModel("里程(KM)", "--"));
        modelList.add(1, new RunningDataModel("热量(卡路里)", "--"));
        modelList.add(POSITION_ALTITUDE, new RunningDataModel("实时海拔(M)", "--"));

        modelList.add(3, new RunningTimeModel("骑行时间(秒)"));


        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(mRecyclerView)
                .layoutManager(new StaggeredGridLayoutManager(mDataRaw, StaggeredGridLayoutManager.VERTICAL))
                .data(modelList)
                .build();
    }

    @Override
    protected void initData() {
        mAMapTrackClient = new AMapTrackClient(MyApplication.getContext());
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
        double accAltitude = location.getAltitude();
        MyLogUtil.d(TAG, "updateAltitudeByLocation: " + accAltitude);
        SimpleModel model = modelList.get(POSITION_ALTITUDE);
        if (model instanceof  RunningDataModel) {
            ((RunningDataModel) model).mData = String.valueOf(Math.round(accAltitude));
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


    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(MyApplication.getContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(MyApplication.getContext());
        }
        Intent nfIntent = new Intent(getContext(), HomeActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(getContext(), 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }
}
