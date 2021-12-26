package com.nhr.weather.Adpter;

import android.content.Context;
import android.util.Log;


import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.nhr.weather.entity.City;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends ArrayAdapter implements Filterable {
    private Context mcontext;
    private int resourceId;
    private List<City> mFilteredList;//过滤后的列表
    private final List<City> all_List;//需要过滤的列表
    private ArrayFilter mFilter;



    public CityAdapter(Context mcontext,int textViewResourceId,List<City> all_List) {
        super(mcontext,textViewResourceId,all_List);
        this.mcontext = mcontext;
        this.all_List = all_List;
        mFilteredList = all_List;
        resourceId=textViewResourceId;
    }

    public List<City> getmFilteredList() {
        return mFilteredList;
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFilteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public class ViewHolder {
        TextView ECity;
        TextView TCity;
        ImageView imageView;
    }



    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {//constraint是用户的输入
            //进行过滤的操作
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                ArrayList<City> list = new ArrayList<>(all_List);
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = constraint.toString().toLowerCase();//大写换成小写
                ArrayList<City> values = new ArrayList<>(all_List);//未过滤前的List
                int count = values.size();
                ArrayList<City> newValues = new ArrayList<>();//被过滤后的匹配的List
                /*以下为过滤的条件,可按照个人的需求修改*/
                for (int i = 0; i < count; i++) {
                    City value = values.get(i);
                    String valueText = value.getName()+value.getCode();//ValueText是每一项筛选的依据

                    if (valueText != null && valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        if (valueText.contains(prefixString)) {
                            newValues.add(value);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //发表过滤的结果
            mFilteredList = (List<City>) results.values;
            //mFilteredList = all_List;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
