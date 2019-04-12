package com.nexuslink.alphrye.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.RunningDataModel;
import com.nexuslink.alphrye.model.RunningTimeModel;
import com.nexuslink.alphrye.ui.activity.MapActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewCycleFragment extends MyLazyFragment {

    public static final int STATUS_READY = 0;

    public static final int STATUS_RUNNING = 1;

    public static final int STATUS_PAUSE = 2;

    public static final int FLAG_TIME_START = 0;

    public static final int FLAG_TIME_PAUSE = 1;

    public static final int FLAG_TIME_CONTINUE = 2;

    public int mCurrentStatus;

    private SimpleAdapter mSimpleAdapter;

    private static final int mDataRaw = 2;

    private  List<SimpleModel> modelList;

    @OnClick(R.id.iv_map) void onMapOpen(View view) {
        Intent intent = new Intent(getContext(), MapActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_start_or_pause) void onStartOrPause(View view) {
        if (mCurrentStatus == STATUS_READY){
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
        } else if (mCurrentStatus == STATUS_RUNNING){
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
            return  -1;
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

    @OnClick(R.id.btn_done) void onDone(View view) {
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

        modelList.add(new RunningDataModel("里程(KM)", "0"));
        modelList.add(new RunningDataModel("热量(焦耳)", "0"));
        modelList.add(new RunningDataModel("骑行次数", "10"));
        modelList.add(new RunningDataModel("平均速度(KM/H)", "0"));
        modelList.add(new RunningDataModel("最高速度(KM/H)", "0"));

        modelList.add(new RunningTimeModel("骑行时间(秒)"));


        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(mRecyclerView)
                .layoutManager(new StaggeredGridLayoutManager(mDataRaw, StaggeredGridLayoutManager.VERTICAL))
                .data(modelList)
                .build();
    }

    @Override
    protected void initData() {

    }

    public static NewCycleFragment newInstance() {
        return new NewCycleFragment();
    }

}
