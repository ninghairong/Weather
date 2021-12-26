/**
  * Copyright 2021 json.cn 
  */
package com.nhr.weather.cn.json.pojo;
import java.util.Date;

/**
 * Auto-generated: 2021-12-01 20:16:20
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class JsonRootBean {

    private String message;
    private int status;
    private String date;
    private String time;
    private CityInfo cityInfo;
    private Data data;
    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setTime(String time) {
         this.time = time;
     }
     public String getTime() {
         return time;
     }

    public void setCityInfo(CityInfo cityInfo) {
         this.cityInfo = cityInfo;
     }
     public CityInfo getCityInfo() {
         return cityInfo;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

}