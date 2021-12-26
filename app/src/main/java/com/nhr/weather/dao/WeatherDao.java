package com.nhr.weather.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nhr.weather.entity.Weather;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM Weather where cityID=:id")
    List<Weather> getWeather(int id);
    @Insert
    long insert(Weather weather);
    @Update
    int update(Weather weather);
    @Delete
    int delete(Weather weather);
}
