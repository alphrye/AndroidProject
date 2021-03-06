package com.nexuslink.alphrye.net.wrapper;

import android.text.TextUtils;

import com.nexuslink.alphrye.api.CommonApiService;
import com.nexuslink.alphrye.api.EagleApiService;
import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.net.bean.CommonNetBean;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static Map<String, RetrofitWrapper> mMap = new HashMap<>();


    public static RetrofitWrapper getInstance () {
        if (mInstance == null) {
            mInstance = new RetrofitWrapper();
        }
        return mInstance;
    }

    public static RetrofitWrapper getInstance (String baseUrl) {
        mInstance = mMap.get(baseUrl);
        if (mInstance == null) {
            mInstance = new RetrofitWrapper(baseUrl);
        }
        return mInstance;
    }

    private RetrofitWrapper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(CommonConstance.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private RetrofitWrapper(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  <T> T createService(Class<T> service){
        return mRetrofit.create(service);
    }


    public CommonApiService getCommonCall() {
        return RetrofitWrapper
                .getInstance()
                .createService(CommonApiService.class);
    }

    public EagleApiService getEagleCall() {
        return RetrofitWrapper
                .getInstance()
                .createService(EagleApiService.class);
    }

    /**
     * 通用异步网络请求
     * @param call
     * @param commonCallBack
     * @param <T>
     */
    public <T> void enqueue(Call<CommonNetBean<T>> call, final CommonCallBack<T> commonCallBack) {
        if (call== null
                || commonCallBack == null) {
            return;
        }
        call.enqueue(new Callback<CommonNetBean<T>>() {
            @Override
            public void onResponse(Call<CommonNetBean<T>> call, Response<CommonNetBean<T>> response) {
                CommonNetBean<T> body = response.body();
                if (body == null) {
                    return;
                }
                if (!"200".equals(body.status)) {
                    String prompts = body.prompts;
                    if (TextUtils.isEmpty(prompts)) {
                        return;
                    }
                    commonCallBack.onFail(prompts);
                    return;
                }
                T bean = body.data;
                if (bean == null) {
                    return;
                }
                commonCallBack.onSuccess(bean);
            }

            @Override
            public void onFailure(Call<CommonNetBean<T>> call, Throwable t) {
                String errorMsg = t.getMessage();
                commonCallBack.onFail(errorMsg);
            }
        });
    }

    public interface CommonCallBack<T> {
        void onSuccess(T response);
        void onFail(String errorTips);
    }
}
