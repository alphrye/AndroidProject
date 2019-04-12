package com.nexuslink.alphrye.model;

public class ProfileModel {

    public UserInfoBean user_info;
    public RideInfoBean ride_info;

    public static class UserInfoBean {
        public String user_name;
        public String user_avatar;
    }

    public static class RideInfoBean {

        public String ride_level;
        public String ride_time;
        public int ride_distance;
    }
}
