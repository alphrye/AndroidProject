package com.nexuslink.alphrye.net.bean;

/**
 *    author : alphrye
 *    time   : 2018/12/29
 *    desc   : 用户数据
 */
public class UserBean {
    public UserInfoBean user_info;
    public RideInfoBean ride_info;

    public static class UserInfoBean {
        public String user_id;
        public String user_name;
        public String user_level_value;
    }

    public static class RideInfoBean {
        public String ride_time;
        public String ride_km;
    }
}
