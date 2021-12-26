package com.nhr.weather.entity;

public class Area {
    private String areaCode;
    private String name;

    public String getAreaCode() {
        return areaCode;
    }

    public Area(String name) {
        this.name = name;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
