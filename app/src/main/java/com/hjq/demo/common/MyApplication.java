package com.hjq.demo.common;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hjq.demo.helper.ActivityStackManager;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengHelper;
import com.nexuslink.alphrye.helper.FlashLightHelper;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 项目中的Application基类
 */
public class MyApplication extends UIApplication {
    public static final float VALUE_LX_OPEN_FLASH_LIGHT = 10.0f;
    private static Context sContext;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;

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

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    float value = event.values[0];
                    Log.d("yuan", "onSensorChanged: " + "当前亮度 " + value + " lx(勒克斯)");
                    // TODO: 2019/1/5  本地sp获取状态，检查手动开启，设置状态
                    FlashLightHelper.getInstance().switchFlashLight(value < VALUE_LX_OPEN_FLASH_LIGHT, null);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensorManager.registerListener(mSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    public static Context getContext() {
        return sContext;
    }
}