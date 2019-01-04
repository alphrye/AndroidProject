package com.nexuslink.alphrye.ui.fragment;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

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

    private boolean isFlashLightOpen;
    private CameraManager mCameraManager;

    public static CycleFragment newInstance() {
        return new CycleFragment();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSwitchFlashLight) {
            switchFlashLight(!isFlashLightOpen);
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
    }

    @Override
    protected void initData() {

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

    /**
     * 闪光灯切换
     * @param b
     */
    private void switchFlashLight(boolean b) {
        //Android版本适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mCameraManager == null) {
                mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    mCameraManager.setTorchMode("0", b);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            isFlashLightOpen = b;
            refreshNextFlashLightStatus(isFlashLightOpen);
        }
    }
}
