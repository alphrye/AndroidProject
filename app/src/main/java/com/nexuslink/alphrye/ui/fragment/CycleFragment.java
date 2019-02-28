package com.nexuslink.alphrye.ui.fragment;

import android.view.View;
import android.widget.Button;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.nexuslink.alphrye.helper.FlashLightHelper;
import com.nexuslink.alphrye.ui.weight.DashboardView;

import butterknife.BindView;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 骑行页面
 */
public class CycleFragment extends MyLazyFragment
        implements View.OnClickListener {
    @BindView(R.id.btn_switch_flash_light)
    Button mBtnSwitchFlashLight;
    @BindView(R.id.v_dash_board)
    DashboardView mDashBoardView;

    private boolean isFlashLightOpen;
    private FlashLightHelper.OnSwitchCallBack mFlashLightSwitchCallBack;

    public static CycleFragment newInstance() {
        return new CycleFragment();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSwitchFlashLight) {
            FlashLightHelper.getInstance().switchFlashLight(!isFlashLightOpen, mFlashLightSwitchCallBack);
        }
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
        refreshNextFlashLightStatus(isFlashLightOpen);
        mBtnSwitchFlashLight.setOnClickListener(this);
        mDashBoardView.updateToValue(15);
    }

    @Override
    protected void initData() {
        mFlashLightSwitchCallBack = new FlashLightHelper.OnSwitchCallBack() {
            @Override
            public void onSwitch(boolean b) {
                isFlashLightOpen = b;
                refreshNextFlashLightStatus(isFlashLightOpen);
            }
        };
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * 刷新下一个闪光灯状态
     * @param isFlashLightOpen
     */
    private void refreshNextFlashLightStatus (boolean isFlashLightOpen) {
        if (mBtnSwitchFlashLight == null) {
            return;
        }
        mBtnSwitchFlashLight.setText(isFlashLightOpen ? "关" : "开");
    }
}
