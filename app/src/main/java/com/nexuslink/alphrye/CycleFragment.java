package com.nexuslink.alphrye;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 骑行页面
 */
public class CycleFragment extends MyLazyFragment
        implements View.OnClickListener {

    public static CycleFragment newInstance() {
        return new CycleFragment();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cycle;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_cycle_title;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
