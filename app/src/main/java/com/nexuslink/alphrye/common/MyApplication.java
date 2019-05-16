package com.nexuslink.alphrye.common;

import android.content.Context;
import android.hardware.Sensor;
import android.support.multidex.MultiDex;

import com.nexuslink.alphrye.helper.ActivityStackManager;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.nexuslink.alphrye.helper.FlashLightHelper;
import com.nexuslink.alphrye.helper.SPUtil;
import com.nexuslink.alphrye.helper.SensorHelper;
import com.nexuslink.alphrye.model.FlashlightChangeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 项目中的Application基类
 */
public class MyApplication extends UIApplication implements SensorHelper.ISensorChangeObserver {

    private static Context sContext;

    private SensorHelper mSensorHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化吐司工具类
        ToastUtils.init(this);

        // 友盟统计
        UmengHelper.init(this);

        // Activity 栈管理
        ActivityStackManager.init(this);

        //Context获取
        sContext = getApplicationContext();

        //传感器监听
        initSensor();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mSensorHelper.removeObservers(this);
        mSensorHelper.unregisterSensorListener();
    }

    @Override
    public void onSensorChange(float[] values, int type) {
        if (type == Sensor.TYPE_LIGHT) {
            float value = values[0];
            // TODO: 2019/1/5  本地sp获取状态，检查手动开启，设置状态
            Boolean isAuto = SPUtil.getBoolean(CommonConstance.SP_STATUS_FLASH, false);
            if (isAuto) {
                FlashLightHelper
                        .getInstance()
                        .switchFlashLight(value < FlashLightHelper.VALUE_LX_OPEN_FLASH_LIGHT, new FlashLightHelper.OnSwitchCallBack() {
                            @Override
                            public void onSwitch(boolean b) {
                                EventBus.getDefault().post(new FlashlightChangeEvent(b));
                            }
                        });
            }
        }
    }

    /**
     * 初始化传感器(初始化后，数据开始获取，添加观察者观察数据)
     */
    private void initSensor() {
        mSensorHelper = SensorHelper.getInstance();
        mSensorHelper.registerSensorListener();
        mSensorHelper.addObservers(this);
    }

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext() {
        return sContext;
    }
}