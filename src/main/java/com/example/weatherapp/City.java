package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;

public class City {
    private  String xcoord;
    private String ycoord;
    private String name;
    private  String region;
    private String[] data;
    public City(String x, String y, String name,String region)
    {
        xcoord=x;
        ycoord=y;
        this.name=name;
        this.region=region;
    }
    public String getXcoord()
    {
        return this.xcoord;
    }
    public String getRegion()
    {
        return this.region;
    }

    public String getYcoord()
    {
        return this.ycoord;
    }
    public String getName()
    {
        return this.name;
    }
    public String[] getData()
    {
        return this.data;
    }
    public void setData(String[] data)
    {
        this.data=data;
    }
    public void eraseData()
    {
        data=null;
    }
    public boolean ifDataNull()
    {
        if(data==null){
            return true;
        }
        return false;
    }
}
