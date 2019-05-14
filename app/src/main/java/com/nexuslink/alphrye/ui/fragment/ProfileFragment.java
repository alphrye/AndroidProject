package com.nexuslink.alphrye.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.MyLogUtil;
import com.nexuslink.alphrye.helper.SPUtil;
import com.nexuslink.alphrye.model.RideHistoryModel;
import com.nexuslink.alphrye.ui.activity.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 个人页面
 */
public class ProfileFragment extends MyLazyFragment implements SimpleAdapter.OnItemClickListener, OnTitleBarListener {
    public static final String TAG = "ProfileFragment";

    private SimpleAdapter mSimpleAdapter;

    private AMapTrackClient mAapTrackClient;

    private long serverId;

    private long terminalId;

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tb_profile_title)
    TitleBar mTitleBar;

    private OnTrackListener trackListener = new OnTrackListener(){

        @Override
        public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

        }

        @Override
        public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

        }

        @Override
        public void onDistanceCallback(DistanceResponse distanceResponse) {

        }

        @Override
        public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

        }

        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

        }

        @Override
        public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
            if (queryTrackResponse.isSuccess()) {
                List<Track> tracks =  queryTrackResponse.getTracks();
                if (tracks != null) {
                    Log.d(TAG, "onQueryTrackCallback: size = " + tracks.size());
                    List<SimpleModel> data = new ArrayList<>();
                    for (int i = 0; i < tracks.size(); i++) {
                        Track curTrack = tracks.get(i);
                        if (curTrack == null) {
                            continue;
                        }
                        data.add(new RideHistoryModel(curTrack));
                        Log.d(TAG, "onQueryTrackCallback: " + i + ": distance = " + curTrack.getDistance() + " id =" +
                                "" + curTrack.getTrid());
                    }
                    mSimpleAdapter.setData(data);
                }
                // 查询成功，tracks包含所有轨迹及相关轨迹点信息
            } else {
                // 查询失败
            }
        }

        @Override
        public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

        }

        @Override
        public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

        }
    };

    @Override
    public void onPageSelect() {
        super.onPageSelect();
        //serverId与terminalId为后端分配
        serverId = SPUtil.getLong(CommonConstance.SP_SERVER_ID);
        terminalId = SPUtil.getLong(CommonConstance.SP_TERMINAL_ID);

        MyLogUtil.d(TAG, "initView: terminal id = " + terminalId + " serverId =" + serverId);

        if (serverId == 0L || terminalId == 0L) {
            return;
        }

        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
                serverId,
                terminalId,
                -1,	// 轨迹id，传-1表示查询所有轨迹
                System.currentTimeMillis() - 12 * 60 * 60 * 1000,
                System.currentTimeMillis(),
                0,      // 不启用去噪
                1,   // 绑路
                0,      // 不进行精度过滤
                DriveMode.DRIVING,  // 当前仅支持驾车模式
                1,     // 距离补偿
                5000,   // 距离补偿，只有超过5km的点才启用距离补偿
                1,  // 结果应该包含轨迹点信息
                1,  // 返回第1页数据，由于未指定轨迹，分页将失效
                100    // 一页不超过100条
        );

        mAapTrackClient.queryTerminalTrack(queryTrackRequest, trackListener);
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_profile_title;
    }

    @Override
    protected void initView() {

        //测试数据
        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(mRecyclerView)
                .itemClickListener(this)
                .build();

        mTitleBar.setOnTitleBarListener(this);

        mAapTrackClient = new AMapTrackClient(MyApplication.getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.d(TAG, "getUserVisibleHint: ");
        return super.getUserVisibleHint();
    }

    @Override
    public boolean isFragmentVisible() {
        Log.d(TAG, "isFragmentVisible: ");
        return super.isFragmentVisible();
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onItemClick(View v, SimpleModel model, int position) {

    }

    @Override
    public void onLeftClick(View v) {

    }

    @Override
    public void onTitleClick(View v) {

    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
    }
}
