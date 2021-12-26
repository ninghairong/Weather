package com.nhr.weather.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhr.weather.cn.json.pojo.Data;
import com.nhr.weather.cn.json.pojo.JsonRootBean;
import com.nhr.weather.entity.Weather;

public class jsonTool {
    private static Data data;

    public static Weather jsonAnalytical(String str){
        Weather weather = new Weather();
        Gson gson = new Gson();  //创建Gson对象
        //将Json字符串转换为jsonBean 实体类
        try{
            JsonRootBean jsonRootBean = gson.fromJson(str, JsonRootBean.class);
            data = jsonRootBean.getData();
            weather.setCityID(Integer.parseInt(jsonRootBean.getCityInfo().getCitykey()));
            if(jsonRootBean.getCityInfo().getParent().equals("")){
                weather.setCityName(jsonRootBean.getCityInfo().getCity());
            }else
            weather.setCityName(jsonRootBean.getCityInfo().getParent()+"-"+jsonRootBean.getCityInfo().getCity());
            weather.setDataTime(jsonRootBean.getDate());
            weather.setHumidity(jsonRootBean.getData().getShidu()+"");
            weather.setTemperature(jsonRootBean.getData().getWendu());
            weather.setPm2_5(jsonRootBean.getData().getPm25());
            weather.setTips(jsonRootBean.getData().getForecast().get(0).getNotice());
            weather.setWeather(jsonRootBean.getData().getForecast().get(0).getType());
            weather.setWeatherID(weather.getCityID());
        }catch (Exception e){
            return null;
        }
         return weather;
    }
}
