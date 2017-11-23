package com.example.administrator.coolweather.util;

import android.provider.ContactsContract;
import android.text.TextUtils;

import com.example.administrator.coolweather.MyApplication;
import com.example.administrator.coolweather.db.City;
import com.example.administrator.coolweather.db.County;
import com.example.administrator.coolweather.db.DataHelper;
import com.example.administrator.coolweather.db.Province;
import com.example.administrator.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/18.
 */

public class Utility {

    private static DataHelper dataHelper;

    {
        dataHelper = new DataHelper();
    }

    /**
     * 处理服务器端返回的省信息
     * @return
     */

    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){     //检测是否String是0或null
                try{
                    JSONArray allProvinces = new JSONArray(response);
                    for(int i=0;i<allProvinces.length();i++){
                        JSONObject provinceObject = allProvinces.getJSONObject(i);
                        Province province  = new Province();
                        province.setProvinceName(provinceObject.getString("name"));
                        province.setProvinceCode(provinceObject.getInt("id"));
                        province.save();
                    }
                    return true;
                }catch(JSONException e){
                    e.printStackTrace();
                }
        }
        return false;
    }

    /**
     * 处理服务器端返回的城市信息
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);

                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 处理服务器端返回的县信息
     * @return
     */

    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray  allCounties = new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * 将传过来的JSON数据转变为Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            /**分析一下JSON的格式，很自然就可以写出来**/
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);         //强大啊！！！
            //只要写好对应的的Weather类，直接调用fromJson就能将JSON转变为相应的类
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
