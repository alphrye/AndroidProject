package com.nexuslink.alphrye.api;

import com.nexuslink.alphrye.model.CommonEagleNetModel;
import com.nexuslink.alphrye.model.FullServerModel;
import com.nexuslink.alphrye.model.ServerListModel;
import com.nexuslink.alphrye.model.SimpleServerModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 猎鹰接口相关
 * @author yunarui
 * @date 2019/4/21
 */
public interface EagleApiService {

    /**
     * 添加服务
     * @param key
     * @param name
     * @return
     */
    @POST("/v1/track/service/add")
    @FormUrlEncoded
    Call<CommonEagleNetModel<SimpleServerModel>> addService(@Field("key") String key,
                                                            @Field("name") String name);

    /**
     * 添加服务
     * @param key
     * @param name
     * @param desc
     * @return
     */
    @POST("/v1/track/service/add")
    @FormUrlEncoded
    Call<CommonEagleNetModel<SimpleServerModel>> addService(@Field("key") String key,
                                                            @Field("name") String name,
                                                            @Field("desc") String desc);

    /**
     * 删除服务
     * @param key
     * @param sid
     * @return
     */
    @POST("/v1/track/service/delete")
    @FormUrlEncoded
    Call<CommonEagleNetModel> deleteService(@Field("key") String key,
                                            @Field("sid") String sid);

    /**
     * 用户可以对某个 Service 的名字、描述进行修改
     * @param key
     * @param sid
     * @param name
     * @param desc
     * @return
     */
    @POST("/v1/track/service/update")
    @FormUrlEncoded
    Call<CommonEagleNetModel<SimpleServerModel>> updateService(@Field("key") String key,
                                                               @Field("sid") String sid,
                                                               @Field("name") String name,
                                                               @Field("desc") String desc);

    /**
     * 获取服务列表
     * @param key
     * @return
     */
    @GET("/v1/track/service/list")
    Call<CommonEagleNetModel<ServerListModel>> listService(@Query("key") String key);

}
