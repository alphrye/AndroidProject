package com.nexuslink.alphrye.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
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
    private SimpleAdapter mSimpleAdapter;

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tb_profile_title)
    TitleBar mTitleBar;

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
        List<SimpleModel> rideHistoryModels = new ArrayList<>();
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        rideHistoryModels.add(new RideHistoryModel());
        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(mRecyclerView)
                .itemClickListener(this)
                .data(rideHistoryModels)
                .build();

        mTitleBar.setOnTitleBarListener(this);
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
