package com.nexuslink.alphrye.api;

import com.nexuslink.alphrye.net.bean.UserBean;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonApiService {
    @GET("/v1/user_profile")
    Call<UserBean> requestUserProfile();
}
