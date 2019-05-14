package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.amap.api.services.help.Tip;
import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.ActivityStackManager;
import com.nexuslink.alphrye.helper.DoubleClickHelper;
import com.nexuslink.alphrye.ui.adapter.HomeFragmentAdapter;
import java.util.List;

import butterknife.BindView;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 主页界面
 */
public class HomeActivity extends MyActivity implements
        ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_SEARCH_TIP = 0;

    public static final int PAGE_HOME_EXPLORE = 0;

    public static final int PAGE_HOME_MAP = 1;

    public static final int PAGE_HOME_PROFILE = 2;

    public static final String TAG = "HomeActivity";

    @BindView(R.id.vp_home_pager)
    ViewPager mViewPager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView mBottomNavigationView;

    private HomeFragmentAdapter mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        mViewPager.addOnPageChangeListener(this);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new HomeFragmentAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setCurrentItem(PAGE_HOME_EXPLORE);
    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        List<MyLazyFragment> fragmentList = mPagerAdapter.getFragmentList();
        if (fragmentList == null) {
            return;
        }
        if (position < fragmentList.size()) {
            MyLazyFragment fragment = fragmentList.get(position);
            if (fragment == null) {
                return;
            }
            fragment.onPageSelect();
        }
        switch (position) {
            case PAGE_HOME_EXPLORE:
                mBottomNavigationView.setSelectedItemId(R.id.home_explore);
                Log.d(TAG, "onPageSelected: home_explore");
                break;
            case PAGE_HOME_MAP:
                mBottomNavigationView.setSelectedItemId(R.id.home_cycle);
                Log.d(TAG, "onPageSelected: home_cycle");
                break;
            case PAGE_HOME_PROFILE:
                mBottomNavigationView.setSelectedItemId(R.id.home_profile);
                Log.d(TAG, "onPageSelected: home_profile");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_explore:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_cycle:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_profile:
                mViewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            getHandler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 进行内存优化，销毁掉所有的界面
                    ActivityStackManager.getInstance().finishAllActivities();
                }
            }, 300);
        } else {
            toast(getResources().getString(R.string.home_exit_hint));
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }

    @Override
    public boolean isSupportSwipeBack() {
        // 不使用侧滑功能
        return !super.isSupportSwipeBack();
    }
}