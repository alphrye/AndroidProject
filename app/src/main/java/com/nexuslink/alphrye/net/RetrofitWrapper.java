package com.nexuslink.alphrye.net;

import com.nexuslink.alphrye.common.CommonConstance;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *    author : alphrye
 *    time   : 2018/12/29
 *    desc   : Retrofit请求封装
 */
public class RetrofitWrapper {
    private static RetrofitWrapper mInstance;
    private Retrofit mRetrofit;

    public static RetrofitWrapper getInstance () {
        if (mInstance == null) {
            return new RetrofitWrapper();
        }
        return mInstance;
    }

    private RetrofitWrapper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(CommonConstance.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T createService(Class<T> service){
        return mRetrofit.create(service);
    }
}
