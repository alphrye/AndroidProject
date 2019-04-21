package com.nexuslink.alphrye.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.MyLogUtil;
import com.nexuslink.alphrye.model.CommonEagleNetModel;
import com.nexuslink.alphrye.model.ServerListModel;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibActivity extends AppCompatActivity {
    public static final String TAG = "LibActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);

        final AMapTrackClient aMapTrackClient = new AMapTrackClient(MyApplication.getContext());
        final OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListener() {

            @Override
            public void onBindServiceCallback(int i, String s) {

            }

            @Override
            public void onStartGatherCallback(int status, String msg) {
                if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
                        status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                    Toast.makeText(LibActivity.this, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LibActivity.this, "定位采集启动异常，" + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackCallback(int status, String msg) {
                if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                        status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                        status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                    // 服务启动成功，继续开启收集上报
                    aMapTrackClient.startGather(this);
                } else {
                    Toast.makeText(LibActivity.this, "轨迹上报服务服务启动异常，" + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStopGatherCallback(int i, String s) {

            }

            @Override
            public void onStopTrackCallback(int i, String s) {

            }
        };

        RetrofitWrapper wrapper = RetrofitWrapper.getInstance(CommonConstance.EAGLE_URL);
//        wrapper.getEagleCall().addService("6cb0ab78c4a87cdd6f11bec9179eb13c", "HelloTest").enqueue(new Callback<CommonEagleNetModel<SimpleServerModel>>() {
//            @Override
//            public void onResponse(Call<CommonEagleNetModel<SimpleServerModel>> call, Response<CommonEagleNetModel<SimpleServerModel>> response) {
//                MyLogUtil.d(TAG, "onResponse: ");
//            }
//
//            @Override
//            public void onFailure(Call<CommonEagleNetModel<SimpleServerModel>> call, Throwable t) {
//                MyLogUtil.d(TAG, "onFailure: ");
//            }
//        });
        wrapper.getEagleCall().listService("6cb0ab78c4a87cdd6f11bec9179eb13c").enqueue(new Callback<CommonEagleNetModel<ServerListModel>>() {
            @Override
            public void onResponse(Call<CommonEagleNetModel<ServerListModel>> call, Response<CommonEagleNetModel<ServerListModel>> response) {
                MyLogUtil.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<CommonEagleNetModel<ServerListModel>> call, Throwable t) {
                MyLogUtil.d(TAG, "onFailure: ");
            }
        });

        wrapper.getEagleCall().deleteService("6cb0ab78c4a87cdd6f11bec9179eb13c", "32798").enqueue(new Callback<CommonEagleNetModel>() {
            @Override
            public void onResponse(Call<CommonEagleNetModel> call, Response<CommonEagleNetModel> response) {
                MyLogUtil.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<CommonEagleNetModel> call, Throwable t) {
                MyLogUtil.d(TAG, "onResponse: ");
            }
        });
    }
}
