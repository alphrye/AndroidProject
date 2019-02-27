package com.hjq.demo.common;

import android.content.Context;
import android.hardware.Sensor;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hjq.demo.helper.ActivityStackManager;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.nexuslink.alphrye.sensor.SensorHelper;

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
            Log.d("Test", "onSensorChange: light:" + values[0]);
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