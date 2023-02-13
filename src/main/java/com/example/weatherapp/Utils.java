package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.UrlQuerySanitizer;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Utils {
    private static SharedPreferences sharedPreferences;
    private static final String SAVED_CITIES_KEY="SAVED_CITIES";
    private static final String ALL_CITIES_KEY="ALL_CITIES";
    private static Context context;
    private static Utils instance;

    public Utils(Context context)
    {
        sharedPreferences=context.getSharedPreferences("alternate_db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        if(null==getSavedCities()) {
            editor.putString(SAVED_CITIES_KEY, gson.toJson(new ArrayList<City>()));
            editor.apply();
        }
        if(null==getAllCities())
        {
            editor.putString(ALL_CITIES_KEY,gson.toJson(populateAllCities(context)));
            editor.apply();
        }
        this.context=context;
    }
    public void resetAllCities(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        editor.remove(ALL_CITIES_KEY);
        editor.apply();
        editor.putString(ALL_CITIES_KEY,gson.toJson(populateAllCities(this.context)));
        editor.apply();
    }

    private ArrayList<City> populateAllCities(Context context)
    {
        class PopulateRunnable implements java.lang.Runnable
        {
            public  ArrayList<City> cities=new ArrayList<>();
            @Override
            public void run() {
                InputStream inputStream=context.getResources().openRawResource(R.raw.worldcities);
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String[] store;
                    try {
                        while ((line=bufferedReader.readLine())!=null)
                        {
                            store=line.split(",");
                                store[0]=store[0].replaceAll("\"","");
                                if(!store[0].equals("city")) {
                                    store[2] = store[2].replaceAll("\"", "");
                                    store[3] = store[3].replaceAll("\"", "");
                                    store[4] = store[4].replaceAll("\"", "");
                                    cities.add(new City(store[2], store[3], store[0], store[4]));
                                }
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            public ArrayList<City> getData(){
                return cities;
            }
        }
        PopulateRunnable populateRunnable=new PopulateRunnable();
        Thread thread=new Thread(populateRunnable);
        thread.run();
        while(populateRunnable.getData()==null);
        return populateRunnable.getData();
    }
    public static ArrayList<City> getAllCities()
    {
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<City>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_CITIES_KEY,null),type);
    }

    public static Utils getInstance(Context context)
    {
    if(instance!=null)
    {
        return instance;
    }
    else
    {
        instance=new Utils(context);
        return instance;
    }
    }

    public static ArrayList<City> getSavedCities()
    {
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<City>>(){}.getType();
        ArrayList<City> arraylist= gson.fromJson(sharedPreferences.getString(SAVED_CITIES_KEY,null),type);
        return arraylist;
    }
    public static boolean addCity(City city)
    {
        ArrayList<City> arraylist=getSavedCities();
        if(arraylist.add(city) ){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(SAVED_CITIES_KEY);
            editor.putString(SAVED_CITIES_KEY,gson.toJson(arraylist));
            editor.apply();
            return true;
        }
        return false;
    }
    public static void clearCities()
    {
        ArrayList<City> arraylist=new ArrayList<>();
        Gson gson=new Gson();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(SAVED_CITIES_KEY);
        editor.putString(SAVED_CITIES_KEY,gson.toJson(arraylist));
        editor.apply();
    }

    public static String[] getCityWeatherInfoSavedCities(City city) throws IOException, JSONException {
        String store2=weatherDataApiConnect(city.getXcoord(),city.getYcoord());
        String store3=timeZoneDataApiConnect(city.getXcoord(),city.getYcoord());
        String[]data=parseWeather(store2,city.getName());
        data[6]=city.getXcoord();
        data[7]=city.getYcoord();
        data[8]=city.getRegion();
        if(store3!=null) {
            data[9] = parseTimeZone(store3);
        }
        return data;
    }
    private static String parseTimeZone(String responseBody) throws JSONException {
        System.out.println(responseBody);
        int store=responseBody.indexOf("formatted");
        return responseBody.substring(store+23,responseBody.length()-5);
    }
//    private static String riseSetDataApiConnect(String x,String y) throws IOException {
//        URL url=new URL("https://api.sunrise-sunset.org/json?lat="+x+"&lng="+y);
//        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
//        StringBuilder stringBuilder=new StringBuilder();
//        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//        String line="";
//        while((line=bufferedReader.readLine())!=null)
//        {
//            stringBuilder.append(line);
//        }
//        return stringBuilder.toString();
//    }
    public static boolean checkIfContainsCity(String x,String y)
    {
        ArrayList<City> arrayList=getSavedCities();
        if(arrayList!=null)
        {
            for(City city:arrayList)
            {
                if(x.equals(city.getName())&&y.equals(city.getRegion()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static String weatherDataApiConnect(String x, String y) throws IOException {
        URL url=new URL("https://api.darksky.net/forecast/b7fde6f5cec14a1a6797096521f5b686/"+x+","+y+"?exclude=currently,minutely,daily,alerts,flags");
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        StringBuffer stringBuffer=new StringBuffer();
        BufferedReader reader;
        int status=urlConnection.getResponseCode();
        if(status>299){
            return null;
        }
        else{
            reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line="";
            while((line=reader.readLine())!=null){
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }
    }
    private static String timeZoneDataApiConnect(String lon,String lat) throws IOException {
        System.out.println(lon+" "+lat);
        URL url=new URL("http://api.timezonedb.com/v2.1/get-time-zone?key=9SY2I9VDPZUJ&format=json&by=position&lat="+lon+"&lng="+lat+"");
        System.out.println(url.toString());
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        StringBuffer stringBuffer=new StringBuffer();
        BufferedReader reader;
        int status=httpURLConnection.getResponseCode();
        if(status>299) {
            System.out.println(status);
            return null;
        }
        else{
            reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line="";
            while((line=reader.readLine())!=null)
            {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }
    }

    private static String[] parseWeather(String responseBody,String city) throws JSONException {
        String[] data=new String[12];
        JSONObject jsonObject=new JSONObject(responseBody);
        JSONArray jsonArray=jsonObject.getJSONObject("hourly").getJSONArray("data");
        data[0]= String.valueOf(jsonArray.getJSONObject(0).getDouble("apparentTemperature"));
        data[1]= String.valueOf(jsonArray.getJSONObject(0).getDouble("time"));
        data[2]= String.valueOf(jsonArray.getJSONObject(0).getString("summary"));
        data[3]= String.valueOf(jsonArray.getJSONObject(0).getDouble("precipProbability"));
        data[4]= String.valueOf(jsonArray.getJSONObject(0).getDouble("humidity"));
        data[5]=city;
        return data;
    }





}
