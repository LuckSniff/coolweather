package com.example.administrator.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/19.
 */

public class Basic {

    //由于JSON中有些名字不适合直接做Java的field，所以使用@SerializedName（..）的方式，让JSON与Java的field建立映射
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}

