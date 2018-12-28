package com.hjq.demo.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.hjq.base.BaseFragmentAdapter;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.fragment.TestFragmentA;
import com.hjq.demo.ui.fragment.TestFragmentB;
import com.hjq.demo.ui.fragment.TestFragmentC;
import com.hjq.demo.ui.fragment.TestFragmentD;
import com.nexuslink.alphrye.CycleFragment;
import com.nexuslink.alphrye.ExploreFragment;
import com.nexuslink.alphrye.ProfileFragment;

import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 主页界面 ViewPager + Fragment 适配器
 */
public final class HomeFragmentAdapter extends BaseFragmentAdapter<MyLazyFragment> {

    public HomeFragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager manager, List<MyLazyFragment> list) {
        list.add(ExploreFragment.newInstance());
        list.add(CycleFragment.newInstance());
        list.add(ProfileFragment.newInstance());
        //测试
        list.add(TestFragmentA.newInstance());
    }
}