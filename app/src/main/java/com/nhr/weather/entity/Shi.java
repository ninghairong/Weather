package com.nhr.weather.entity;


import java.util.List;

public class Shi {
    private List<Area> areaArrayList;
    private String name;
    private String shiCode;

    public Shi(String name) {
        this.name = name;
    }

    public List<Area> getAreaArrayList() {
        return areaArrayList;
    }

    public void setAreaArrayList(List<Area> areaArrayList) {
        this.areaArrayList = areaArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShiCode() {
        return shiCode;
    }

    public void setShiCode(String shiCode) {
        this.shiCode = shiCode;
    }
    @Override
    public String toString() {
        return name;
    }
}
