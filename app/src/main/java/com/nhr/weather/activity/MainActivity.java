package com.nhr.weather.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nhr.weather.Adpter.FragmentAdapter;
import com.nhr.weather.R;
import com.nhr.weather.entity.City;
import com.nhr.weather.fragment.DetialFragment;
import com.nhr.weather.util.SlidingIndicator;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final int ADD = 1;
    private Map<String,?> map;
    private ArrayList<Fragment> fragmentArrayList;
    ViewPager fgContainer ;
    private SlidingIndicator slidingIndicator;

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("TAG", "onResume: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragmentArrayList = new ArrayList<Fragment>();
        setContentView(R.layout.activity_main);
        ImageView add = findViewById(R.id.add);
        fgContainer = findViewById(R.id.viewPager);
         slidingIndicator = findViewById(R.id.indicator);
        SharedPreferences sharedPreferences = Objects.requireNonNull(getSharedPreferences("city_info", Context.MODE_PRIVATE));
        map = sharedPreferences.getAll();
        DetialFragment detialFragment ;

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            int mapValue = (int) entry.getValue();
            detialFragment = DetialFragment.newInstance(mapValue,mapKey,true);
            fragmentArrayList.add(detialFragment);
            System.out.println(mapKey + "：" + mapValue);
        }
        if(fragmentArrayList.size()==0){
            detialFragment =  DetialFragment.newInstance(101161203,"甘南藏族自治州-卓尼县",false);
            fragmentArrayList.add(detialFragment);
        }

        slidingIndicator.setNumber(fragmentArrayList.size());
        slidingIndicator.slidingIndicatorShow();
        fgContainer.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentArrayList));

        fgContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                slidingIndicator.setViewLayoutParams(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD && resultCode == RESULT_OK) {
            City city = (City) data.getSerializableExtra("city_selected");
            System.out.println(city);
            DetialFragment detialFragment = DetialFragment.newInstance(city.getCode(), city.getName(), false);
            fragmentArrayList.add(detialFragment);
            fgContainer.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentArrayList));
            fgContainer.setCurrentItem(fragmentArrayList.size()-1);
            Log.e("TAG", "onActivityResult: ");
        }
        if (requestCode == ADD && resultCode == 2565) {
            int position = (int) data.getSerializableExtra("city_selected");
            fgContainer.setCurrentItem(position);
        }
    }
}