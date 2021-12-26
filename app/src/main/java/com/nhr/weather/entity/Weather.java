package com.nhr.weather.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Weather {
    //（至少要显示省、市、日期、数据更新时间、温度、湿度、PM2.5等数据）
    @PrimaryKey(autoGenerate = true)
    private int weatherID;
    private int cityID;
    private String weather;
    private String dataTime;
    private String temperature;
    private String humidity;
    private int pm2_5;
    private String tips;
    private String cityName;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDataTime() {
        return dataTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(int weatherID) {
        this.weatherID = weatherID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherID=" + weatherID +
                ", cityID=" + cityID +
                ", dataTime='" + dataTime + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pm2_5=" + pm2_5 +
                ", tips='" + tips + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
