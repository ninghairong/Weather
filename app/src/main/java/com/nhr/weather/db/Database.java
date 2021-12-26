package com.nhr.weather.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nhr.weather.dao.WeatherDao;
import com.nhr.weather.entity.Weather;


@androidx.room.Database(entities = { Weather.class }, version = 1,exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final String DB_NAME = "WeatherDatabase.db";
    private static volatile Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static Database create(final Context context) {
       // Room.databaseBuilder(context,Database.class,DB_NAME).allowMainThreadQueries().build();
        return Room.databaseBuilder(
                context,
                Database.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract WeatherDao getWeatherDao();
}