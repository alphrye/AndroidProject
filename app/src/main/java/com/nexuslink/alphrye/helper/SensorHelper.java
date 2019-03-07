package com.nexuslink.alphrye.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.nexuslink.alphrye.common.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : alphrye
 *    time   : 2019/2/27
 *    desc   : 传感器封装
 */
public class SensorHelper implements SensorEventListener {

    private static SensorHelper sInstance;
    private SensorManager mSensorManager;
    private List<ISensorChangeObserver> mObservers;
    /**光线传感器*/
    private Sensor mLightSensor;
    /**线性加速度传感器(去除重力影响)*/
    private Sensor mLinearAccelerationSensor;
    /**方向传感器*/
    private Sensor mOrientationSensor;

    private SensorHelper() {
        mSensorManager = (SensorManager) MyApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        mObservers = new ArrayList<>();
        initSensors();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (ISensorChangeObserver observer : mObservers) {
            observer.onSensorChange(event.values, event.sensor.getType());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static SensorHelper getInstance() {
        if (sInstance == null) {
            sInstance = new SensorHelper();
        }
        return sInstance;
    }

    /**
     * 传感器初始化
     */
    private void initSensors() {
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mLinearAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    /**
     * 注册传感器监听
     */
    public void registerSensorListener() {
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 取消注册传感器监听
     */
    public void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    /**
     * 添加传感器数据观察者
     * @param sensorChange
     */
    public void addObservers(ISensorChangeObserver sensorChange) {
        mObservers.add(sensorChange);
    }

    /**
     * 删除传感器数据观察
     * @param sensorChange
     */
    public void removeObservers (ISensorChangeObserver sensorChange) {
        mObservers.remove(sensorChange);
    }

    public interface ISensorChangeObserver {
        void onSensorChange(float[] values, int type);
    }
}
