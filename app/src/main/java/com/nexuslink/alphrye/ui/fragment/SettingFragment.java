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

    @BindView(R.id.switch_speed)
    Switch mSwitchSpeed;

    @BindView(R.id.switch_test)
    Switch mSwitchTest;

    private boolean isAuto;

    private boolean isSpeedOn;

    private boolean isTest;

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

        isSpeedOn = SPUtil.getBoolean(CommonConstance.SP_STATUS_SPEED, true);
        mSwitchSpeed.setChecked(isSpeedOn);

        mSwitchSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSpeedOn = !isSpeedOn;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_SPEED, isSpeedOn);
            }
        });

        isTest = SPUtil.getBoolean(CommonConstance.SP_STATUS_TEST, false);
        mSwitchTest.setChecked(isTest);

        mSwitchTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTest = !isTest;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_TEST, isTest);
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
