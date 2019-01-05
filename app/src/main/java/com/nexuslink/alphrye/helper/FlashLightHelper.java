package com.nexuslink.alphrye.helper;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import com.hjq.demo.common.MyApplication;

/**
 *    author : alphrye
 *    time   : 2019/1/5
 *    desc   : 闪光灯帮助类
 */
public class FlashLightHelper {

    private static FlashLightHelper mInstance;
    private CameraManager mCameraManager;

    public static FlashLightHelper getInstance() {
        if (mInstance == null) {
            mInstance = new FlashLightHelper();
        }
        return mInstance;
    }

    private FlashLightHelper() {
        //低版本适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager = (CameraManager) MyApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
        }
    }

    /**
     * 切换闪光灯状态
     * @param b
     * @param callBack
     */
    public void switchFlashLight(boolean b, OnSwitchCallBack callBack) {
        if (mCameraManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode("0", b);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        if (callBack == null) {
            return;
        }
        callBack.onSwitch(b);
    }

    /**
     * 闪光灯状态切换回调
     */
    public interface OnSwitchCallBack {
        void onSwitch(boolean b);
    }
}
