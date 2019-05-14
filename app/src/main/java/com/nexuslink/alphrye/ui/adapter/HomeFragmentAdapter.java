package com.nexuslink.alphrye.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.hjq.base.BaseFragmentAdapter;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.ui.fragment.MapFragment;
import com.nexuslink.alphrye.ui.fragment.NewCycleFragment;
import com.nexuslink.alphrye.ui.fragment.ProfileFragment;

import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 主页界面 ViewPager + Fragment 适配器
 */
public final class HomeFragmentAdapter extends BaseFragmentAdapter<MyLazyFragment> {
    private List<MyLazyFragment> mFragmentList;

    public HomeFragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager manager, List<MyLazyFragment> list) {
//        list.add(ExploreFragment.newInstance());
        list.add(MapFragment.newInstance());
        list.add(NewCycleFragment.newInstance());
        list.add(ProfileFragment.newInstance());
        mFragmentList = list;
    }

    public List<MyLazyFragment> getFragmentList() {
        return mFragmentList;
    }
}