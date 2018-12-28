package com.nexuslink.alphrye;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 个人页面
 */
public class ProfileFragment extends MyLazyFragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
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
