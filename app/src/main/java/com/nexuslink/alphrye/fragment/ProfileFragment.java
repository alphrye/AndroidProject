package com.nexuslink.alphrye.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.nexuslink.alphrye.activity.MyAboutActivity;
import com.nexuslink.alphrye.activity.MyExploreActivity;
import com.nexuslink.alphrye.activity.RideHistoryActivity;
import com.nexuslink.alphrye.activity.SettingActivity;

import butterknife.BindView;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 个人页面
 */
public class ProfileFragment extends MyLazyFragment implements View.OnClickListener {
    @BindView(R.id.ll_ride_history)
    LinearLayout mLlRideHistory;
    @BindView(R.id.ll_my_explore)
    LinearLayout mLlMyExplore;
    @BindView(R.id.ll_about)
    LinearLayout mLlAbout;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;

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
        mLlRideHistory.setOnClickListener(this);
        mLlMyExplore.setOnClickListener(this);
        mLlAbout.setOnClickListener(this);
        mLlSetting.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == mLlRideHistory) {
            startActivity(RideHistoryActivity.class);
        } else if (v == mLlMyExplore) {
            startActivity(MyExploreActivity.class);
        } else if (v == mLlAbout) {
            startActivity(MyAboutActivity.class);
        } else if (v == mLlSetting) {
            startActivity(SettingActivity.class);
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
