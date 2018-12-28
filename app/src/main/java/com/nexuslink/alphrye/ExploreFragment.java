package com.nexuslink.alphrye;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 探索页面
 */
public class ExploreFragment extends MyLazyFragment {

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_explore_title;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
