package com.nhr.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhr.weather.R;
import com.nhr.weather.dao.WeatherDao;
import com.nhr.weather.db.Database;
import com.nhr.weather.entity.Weather;
import com.nhr.weather.util.jsonTool;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "isFavorite";

    // TODO: Rename and change types of parameters
    private int code;
    private String name;
    private boolean isFavorite;
    private TextView cityName;
    private TextView updateTime;
    private TextView type;
    private TextView temperature;
    private TextView humidity;
    private TextView pm2_5;
    private TextView tips;
    private ImageView icon_weather;
    private WeatherDao weatherDao;
    private Button add;


    public DetialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param code       Parameter 1.
     * @param name       Parameter 2.
     * @param isFavorite Parameter 3.
     * @return A new instance of fragment DetialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetialFragment newInstance(int code, String name, boolean isFavorite) {
        DetialFragment fragment = new DetialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, code);
        args.putString(ARG_PARAM2, name);
        args.putBoolean(ARG_PARAM3, isFavorite);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getInt(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            isFavorite = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detial, container, false);
        add = view.findViewById(R.id.add_to_list);
        cityName = view.findViewById(R.id.city_name);
        updateTime = view.findViewById(R.id.update_time);
        type = view.findViewById(R.id.type);
        temperature = view.findViewById(R.id.temperature);
        humidity = view.findViewById(R.id.humidity);
        pm2_5 = view.findViewById(R.id.pm2_55);
        tips = view.findViewById(R.id.tips);
        icon_weather = view.findViewById(R.id.icon_weather);
        /*先查数据库，查不到再在线请求*/
        weatherDao = Database.getInstance(getActivity()).getWeatherDao();

        List<Weather> dbWeather = weatherDao.getWeather(code);

        if (dbWeather.isEmpty()) {
            setView(getData());
        } else {
            setView(dbWeather.get(0));
        }



        Button refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                setView(getData());
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("city_info", Context.MODE_PRIVATE).edit();

                if (isFavorite) {
                    editor.remove(name);
                    editor.apply();
                    Toast.makeText(getActivity(), "移除成功", Toast.LENGTH_SHORT).show();

                    add.setText("收藏");
                    isFavorite = false;
                } else {
                    Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                    editor.putInt(name, code);
                    editor.apply();
                    add.setText("移除");
                    isFavorite = true;

                }

            }
        });
        if (isFavorite) {
            add.setText("移除");
        }
        return view;
    }

    private Weather getData() {
        final String[] temp = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://t.weather.itboy.net/api/weather/city/" + code)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                temp[0] = null;
                try {
                    temp[0] = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                   // Toast.makeText(getActivity(), "网络请求失败！", Toast.LENGTH_SHORT).show();
                }
                System.out.println("我是结果" + temp[0]);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (temp[0] == null) {

            Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();

            return null;
        }
        Weather weather = jsonTool.jsonAnalytical(temp[0]);
        if (weather == null) {
            Toast.makeText(getActivity(), "获取失败！", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            weatherDao.insert(weather);
        } catch (SQLiteConstraintException e) {
            weatherDao.update(weather);
        }
        Toast.makeText(getActivity(), "刷新成功！", Toast.LENGTH_SHORT).show();
        return weather;
    }

    private void setView(Weather weather) {
        if (weather == null) {
            return;
        }
        Weather tempWeather = weather;
        cityName.setText(tempWeather.getCityName());
        updateTime.setText(tempWeather.getDataTime());
        type.setText(tempWeather.getWeather());
        temperature.setText(tempWeather.getTemperature() + "℃");
        humidity.setText(tempWeather.getHumidity());
        pm2_5.setText(tempWeather.getPm2_5() + "%");
        tips.setText(tempWeather.getTips());
        switch (tempWeather.getWeather()) {
            case "晴":
                icon_weather.setImageResource(R.drawable.riqing);
                break;
            case "多云":
                icon_weather.setImageResource(R.drawable.rijianduoyun);
                break;
            case "小雨":
                icon_weather.setImageResource(R.drawable.xiaoyu);
                break;
            case "小到中雨":
                icon_weather.setImageResource(R.drawable.xiaodaozhongyu);
                break;
            case "中雨":
                icon_weather.setImageResource(R.drawable.zhongyu);
                break;
            case "中到大雨":
                icon_weather.setImageResource(R.drawable.zhongdaodayu);
                break;
            case "大雨":
                icon_weather.setImageResource(R.drawable.dayu);
                break;
            case "大到暴雨":
                icon_weather.setImageResource(R.drawable.dadaobaoyu);
                break;
            case "暴雨":
                icon_weather.setImageResource(R.drawable.baoyu);
                break;
            case "暴雨到大暴雨":
                icon_weather.setImageResource(R.drawable.baoyudaodabaoyu);
                break;
            case "大暴雨":
                icon_weather.setImageResource(R.drawable.dabaoyu);
                break;
            case "大暴雨到特大暴雨":
                icon_weather.setImageResource(R.drawable.dabaoyudaotedabaoyu);
                break;
            case "特大暴雨":
                icon_weather.setImageResource(R.drawable.tedabaoyu);
                break;
            case "冻雨":
                icon_weather.setImageResource(R.drawable.dongyu);
                break;
            case "阵雨":
                icon_weather.setImageResource(R.drawable.zhenyusvg);
                break;
            case "雷阵雨":
                icon_weather.setImageResource(R.drawable.leizhenyu);
                break;
            case "雨夹雪":
                icon_weather.setImageResource(R.drawable.yujiaxue);
                break;
            case "雷阵雨伴有冰雹":
                icon_weather.setImageResource(R.drawable.leizhenyubanyoubingbao);
                break;
            case "小雪":
                icon_weather.setImageResource(R.drawable.xiaoxue);
                break;
            case "小到中雪":
                icon_weather.setImageResource(R.drawable.xiaodaozhongxue);
                break;
            case "中雪":
                icon_weather.setImageResource(R.drawable.zhongxue);
                break;
            case "中到大雪":
                icon_weather.setImageResource(R.drawable.zhongdaodaxue);
                break;
            case "大雪":
                icon_weather.setImageResource(R.drawable.daxue);
                break;
            case "大到暴雪":
                icon_weather.setImageResource(R.drawable.dadaobaoxue);
                break;
            case "暴雪":
                icon_weather.setImageResource(R.drawable.baoxue);
                break;
            case "阵雪":
                icon_weather.setImageResource(R.drawable.zhenxue);
                break;
            case "阴":
                icon_weather.setImageResource(R.drawable.yin);
                break;
            case "强沙尘暴":
                icon_weather.setImageResource(R.drawable.qiangshachenbao);
                break;
            case "扬沙":
                icon_weather.setImageResource(R.drawable.yangsha);
                break;
            case "沙尘暴":
                icon_weather.setImageResource(R.drawable.shachenbao);
                break;
            case "浮尘":
                icon_weather.setImageResource(R.drawable.fuchen);
                break;
            case "雾":
                icon_weather.setImageResource(R.drawable.wu);
                break;
            case "霾":
                icon_weather.setImageResource(R.drawable.mai);
                break;
            default:
                icon_weather.setImageResource(R.drawable.nop);
        }
    }
}