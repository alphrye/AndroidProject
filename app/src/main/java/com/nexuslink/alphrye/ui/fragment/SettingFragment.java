package com.nexuslink.alphrye.ui.fragment;

import android.view.View;
import android.widget.Switch;

import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.SPUtil;

import butterknife.BindView;

/**
 * @author yuanrui
 * @date 2019/5/15
 */
public class SettingFragment extends MyLazyFragment {

    @BindView(R.id.switch_auto_flash)
    Switch mSwithcAutoflash;

    private boolean isAuto;

    public static MyLazyFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_setting_title;
    }

    @Override
    protected void initView() {
        isAuto = SPUtil.getBoolean(CommonConstance.SP_STATUS_FLASH, false);
        mSwithcAutoflash.setChecked(isAuto);

        mSwithcAutoflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAuto = !isAuto;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_FLASH, isAuto);
            }
        });
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
