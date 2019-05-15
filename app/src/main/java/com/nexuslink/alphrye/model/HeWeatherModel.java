package com.nexuslink.alphrye.model;

import java.util.List;

/**
 * @author yuanrui
 * @date 2019/5/15
 */
public class HeWeatherModel {

    public List<HeWeather6Bean> HeWeather6;

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101040100","location":"重庆","parent_city":"重庆","admin_area":"重庆","cnty":"中国","lat":"29.56376076","lon":"106.55046082","tz":"+8.00"}
         * update : {"loc":"2019-05-15 10:33","utc":"2019-05-15 02:33"}
         * status : ok
         * now : {"cloud":"91","cond_code":"101","cond_txt":"多云","fl":"23","hum":"92","pcpn":"0.0","pres":"978","tmp":"21","vis":"3","wind_deg":"92","wind_dir":"东风","wind_sc":"1","wind_spd":"2"}
         */

        public BasicBean basic;
        public UpdateBean update;
        public String status;
        public NowBean now;

        public static class BasicBean {
            /**
             * cid : CN101040100
             * location : 重庆
             * parent_city : 重庆
             * admin_area : 重庆
             * cnty : 中国
             * lat : 29.56376076
             * lon : 106.55046082
             * tz : +8.00
             */

            public String cid;
            public String location;
            public String parent_city;
            public String admin_area;
            public String cnty;
            public String lat;
            public String lon;
            public String tz;
        }

        public static class UpdateBean {
            /**
             * loc : 2019-05-15 10:33
             * utc : 2019-05-15 02:33
             */

            public String loc;
            public String utc;
        }

        public static class NowBean {
            /**
             * cloud : 91
             * cond_code : 101
             * cond_txt : 多云
             * fl : 23
             * hum : 92
             * pcpn : 0.0
             * pres : 978
             * tmp : 21
             * vis : 3
             * wind_deg : 92
             * wind_dir : 东风
             * wind_sc : 1
             * wind_spd : 2
             */

            public String cloud;
            public String cond_code;
            public String cond_txt;
            public String fl;
            public String hum;
            public String pcpn;
            public String pres;
            public String tmp;
            public String vis;
            public String wind_deg;
            public String wind_dir;
            public String wind_sc;
            public String wind_spd;
        }
    }
}
