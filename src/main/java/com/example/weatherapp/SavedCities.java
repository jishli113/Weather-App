package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedCities extends AppCompatActivity {
    private ImageView backButton;
private RecyclerView savedCitiesRecView;
private ImageView clearSavedCities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_cities);
        savedCitiesRecView=findViewById(R.id.savedCitiesRV);
        backButton=findViewById(R.id.savedCitiesBackButton);
//        ExecutorService executorService= Executors.newFixedThreadPool(100);
//        for(int i=0;i<Utils.getInstance(this).getSavedCities().size();i++)
//        {
//            getWeatherInfo r=new getWeatherInfo(Utils.getInstance(this).getSavedCities().get(i));
//            executorService.execute(r);
//            while(r.dataNull()){
//            }
//            String[] data=r.getData();
//            Utils.addCityInfo(data);
//        }
        SavedCitiesAdapter adapter=new SavedCitiesAdapter(this);
        adapter.setcities(Utils.getInstance(this).getSavedCities());
        savedCitiesRecView.setAdapter(adapter);
        savedCitiesRecView.setLayoutManager(new LinearLayoutManager(this));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearSavedCities=findViewById(R.id.clearSavedCities);
        clearSavedCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance(SavedCities.this).clearCities();
                adapter.setcities(Utils.getInstance(SavedCities.this).getSavedCities());
            }
        });
    }
//    class getWeatherInfo implements Runnable
//    {
//       City city;
//       String data[];
//        public getWeatherInfo(City city){
//            this.city=city;
//        }
//        @Override
//        public void run() {
//            try {
//                data=Utils.getInstance(SavedCities.this).getCityWeatherInfoSavedCities(this.city);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public String[] getData()
//        {
//            return data;
//        }
//        public boolean dataNull()
//        {
//            if(data==null)
//            {
//                return true;
//            }
//            return false;
//        }
//
//    }
}