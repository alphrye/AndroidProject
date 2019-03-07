package com.nexuslink.alphrye.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.nexuslink.alphrye.common.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : alphrye
 *    time   : 2019/3/7
 *    desc   : GPS位置信息相关封装
 */
public class LocationHelper implements LocationListener {

    private static LocationHelper sInstance;
    private LocationManager mLocationManager;
    private List<ILocationChangeObserver> mObservers;

    private LocationHelper() {
        mLocationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        mObservers = new ArrayList<>();
    }

    public void start() {
        //权限检查
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        // TODO: 2019/3/7  了解GPS定位与网络定位
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1.0f, this);
    }

    public static LocationHelper getInstance() {
        if (sInstance == null) {
            sInstance = new LocationHelper();
        }
        return sInstance;
    }

    /**
     * 添加位置数据观察者
     * @param observer
     */
    public void addObservers(ILocationChangeObserver observer) {
        mObservers.add(observer);
    }

    /**
     * 删除位置数据观察
     * @param observer
     */
    public void removeObservers (ILocationChangeObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void onLocationChanged(Location location) {
        for (ILocationChangeObserver observer : mObservers) {
            observer.onLocationChange(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface ILocationChangeObserver {
        void onLocationChange(Location location);
    }
}
