package com.nhr.weather.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.nhr.weather.Adpter.CityAdapter;
import com.nhr.weather.R;
import com.nhr.weather.entity.Area;
import com.nhr.weather.entity.City;
import com.nhr.weather.entity.Provice;
import com.nhr.weather.entity.Shi;
import com.nhr.weather.fragment.DetialFragment;
import com.nhr.weather.util.MyAutoCompleteTextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private static final int RESULT_OK_2 = 2565;
    private ArrayList<Provice> proviceList;
    ArrayList<ArrayList<Shi>> shiList;
    ArrayList<ArrayList<ArrayList<Area>>> areaList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ArrayList<City> cityList = null;
        Gson gson = new Gson();  //创建Gson对象
        //将Json字符串转换为jsonBean 实体类
        AssetManager assetManager = this.getAssets();
        try {
            InputStream input = assetManager.open("city.json");
            InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
            cityList = gson.fromJson(isr, new ArrayList<City>() {
            }.getClass().getGenericSuperclass());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //异步准备城市选择器
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = AddActivity.this.getAssets();
                try {
                    InputStream input = assetManager.open("selected.json");
                    InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
                    proviceList = gson.fromJson(isr, new ArrayList<Provice>() {
                    }.getClass().getGenericSuperclass());
                    System.out.println(proviceList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ImageView locationButton = findViewById(R.id.cityPicker);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //关闭软件盘
              //  https://blog.csdn.net/jing110fei/article/details/41863821
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }




                fromat();
                showPickerView();
            }
        });


        //  System.out.println(cityList);
        @SuppressLint("WrongViewCast") MyAutoCompleteTextView at_textview = findViewById(R.id.at_textview);
        CityAdapter adapter = new CityAdapter(this, android.R.layout.simple_dropdown_item_1line, cityList);
        /*将ArrayAdapter添加到AutoCompleteTextView中*/
        at_textview.setAdapter(adapter);
        at_textview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<City> citys = adapter.getmFilteredList();
                City city = citys.get(i);
                Intent intent = new Intent();
                intent.putExtra("city_selected", city);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ListView favoriteCityList = findViewById(R.id.favorite_list);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getSharedPreferences("city_info", Context.MODE_PRIVATE));
        Map<String, ?> map = sharedPreferences.getAll();
        ArrayList<City> cityArrayList = new ArrayList<>();
        City city = new City(101161203, "甘南藏族自治州-卓尼县");
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            int mapValue = (int) entry.getValue();
            city = new City(mapValue, mapKey);
            cityArrayList.add(city);
            System.out.println(mapKey + "：" + mapValue);
        }
        if (cityArrayList.size() == 0) {
            cityArrayList.add(city);
        }
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityArrayList);
        favoriteCityList.setAdapter(adapter1);
        favoriteCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                List<City> citys = (List<City>) ;
                City city = (City) adapter1.getItem(i);
                Intent intent = new Intent();
                intent.putExtra("city_selected", i);
                setResult(RESULT_OK_2, intent);
                finish();
            }
        });

    }
    //https://blog.csdn.net/qq_35123404/article/details/82463069


    private void fromat() {
        shiList = new ArrayList<>();//二级
        areaList = new ArrayList<>();
        for (int i = 0; i < proviceList.size(); i++) {
            ArrayList<ArrayList<Area>> area2 = new ArrayList<>();
            ArrayList<Shi> shis = new ArrayList<>();
            for (int j = 0; j < proviceList.get(i).getShiList().size(); j++) {
                shis.add(proviceList.get(i).getShiList().get(j));
                ArrayList<Area> areas = new ArrayList<>();
                for (int k = 0; k < proviceList.get(i).getShiList().get(j).getAreaArrayList().size(); k++) {
                    areas.add(proviceList.get(i).getShiList().get(j).getAreaArrayList().get(k));
                }
                area2.add(areas);
            }
            areaList.add(area2);
            shiList.add(shis);
        }
    }

    private void showPickerView() {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {








                //返回的分别是三个级别的选中位置
                Log.e("TAG", "onOptionsSelect: "+options1+"   "+ options2 +  "   "+options3);



               Area area =  proviceList.get(options1).getShiList().get(options2).getAreaArrayList().get(options3);
               if(area.getAreaCode().equals("-2019012565")){
                   area.setAreaCode(proviceList.get(options1).getShiList().get(options2).getShiCode());
               }
                if(area.getAreaCode().equals("-2019012565")){
                    area.setAreaCode(proviceList.get(options1).getPrCode());
                }
                City city = new City(Integer.parseInt(area.getAreaCode()),proviceList.get(options1).getShiList().get(options2).getName()+"-"+area.getName());
                Intent intent = new Intent();
                intent.putExtra("city_selected", city);
                setResult(RESULT_OK, intent);
                finish();


            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //  pvOptions.setPicker(proviceList, proviceList, options3Items);//三级选择器
        pvOptions.setPicker(proviceList, shiList,areaList);
        pvOptions.show();
    }


}