package com.nhr.weather.entity;

import java.util.List;

public class Provice {
    private String name;
    private String prCode;
    private List<Shi> shiList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrCode() {
        return prCode;
    }

    public void setPrCode(String prCode) {
        this.prCode = prCode;
    }

    public List<Shi> getShiList() {
        return shiList;
    }

    public void setShiList(List<Shi> shiList) {
        this.shiList = shiList;
    }

    @Override
    public String toString() {
        return name;
    }
}
