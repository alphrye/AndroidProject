package com.nexuslink.alphrye.api;

import com.nexuslink.alphrye.model.FeedModel;
import com.nexuslink.alphrye.model.ProfileModel;
import com.nexuslink.alphrye.model.RideHistoryModel;
import com.nexuslink.alphrye.net.bean.CommonNetBean;
import com.nexuslink.alphrye.net.bean.UserBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *    author : alphrye
 *    time   : 2018/12/29
 *    desc   : 通用请求API
 */
public interface CommonApiService {
    @GET("/v1/user_profile/")
    Call<CommonNetBean<UserBean>> requestUserProfile();

    @GET("/v1/feed/")
    Call<CommonNetBean<List<FeedModel>>> requestFeeds();

    @GET("/v1/my_feed/")
    Call<CommonNetBean<List<FeedModel>>> requestMyFeeds();

    @GET("/v1/my_ride_history/")
    Call<CommonNetBean<List<RideHistoryModel>>> requestMyRideHistory();

    @GET("/v1/login/")
    Call<CommonNetBean<ProfileModel>> login();
}
