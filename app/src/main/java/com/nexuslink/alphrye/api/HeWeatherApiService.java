package com.nexuslink.alphrye.api;

import com.nexuslink.alphrye.model.HeWeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author yuanrui
 * @date 2019/5/15
 */
public interface HeWeatherApiService {

    @GET("/s6/weather/now/")
    Call<HeWeatherModel> nowWeather(@Query("location") String location, @Query("key") String key);

}
