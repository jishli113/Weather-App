package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.droidsonroids.gif.GifImageView;

public class WeatherScreen extends AppCompatActivity {
    private static TextView city;
    private static TextView temperature;
    private static TextView summary;
    private static TextView humidity;
    private static TextView precipPossibility;
    private  GifImageView gifImageView;
    private static ImageView backButton;
    private static TextView region;
    private static FloatingActionButton addButton;
    private static String[] data;
    private static ImageView plus;
    private static TextView time;
    private View layout;
    private static Drawable daytime;
    private static Drawable nighttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_screen);
        initView();
        if (data[2].contains("Cloudy") || data[2].contains("cloudy") || data[2].contains("Overcast")) {
            gifImageView.setImageResource(R.drawable.cloudy2);
        } else if (data[2].contains("Rain") || data[2].contains("Drizzle")) {
            gifImageView.setImageResource(R.drawable.rainy2a);
        } else if (data[2].contains("Sun")||data[2].equals("Clear")) {
            gifImageView.setImageResource(R.drawable.sunny2);
        } else if (data[2].contains("Snow")) {
            gifImageView.setImageResource(R.drawable.snowy2);
        }
    }
    public void initView()
    {
        addButton=findViewById(R.id.floatingActionButton2);
        plus=findViewById(R.id.plus);
        Bundle bundle = getIntent().getExtras();
        if (bundle.get("SEARCH_SCREEN")!=null) {
            data = (String[]) bundle.get("SEARCH_SCREEN");
            checkAddButton checkAddButton=new checkAddButton();
            Thread thread=new Thread(checkAddButton);
            thread.start();
            addButton.setEnabled(checkAddButton.ret);
        }
        if(bundle.get("SAVED_CITIES")!=null)
        {
            data=(String[]) bundle.get("SAVED_CITIES");
            addButton.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.INVISIBLE);
        }
        city = findViewById(R.id.txtCity);
        temperature = findViewById(R.id.txtTemperature);
        summary = findViewById(R.id.txtSummary);
        humidity = findViewById(R.id.txtHumidNumber);
        precipPossibility = findViewById(R.id.txtPrecipPossibilityNumber);
        gifImageView=findViewById(R.id.gifImageView);
        backButton=findViewById(R.id.weather_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        region=findViewById(R.id.txtRegion);
        city.setText(data[5]);
        temperature.setText(data[0]);
        summary.setText(data[2]);
        precipPossibility.setText(data[3]+"%");
        humidity.setText(data[4]);
        region.setText(data[8]);
        time=findViewById(R.id.txtTime);
        time.setText(data[9]);
        layout=findViewById(R.id.contraintLayout);
        daytime=getResources().getDrawable(R.drawable.daytime);
        nighttime=getResources().getDrawable(R.drawable.nighttime);
        int store=1;
        if(data[9].length()==5)
        {
            store=2;
        }
        if(Double.parseDouble(data[9].substring(0,store))>=7&&Double.parseDouble(data[9].substring(0,store))<=18)
        {
                layout.setBackground(daytime);
        }
        else{
            layout.setBackground(nighttime);
        }
    }

    public class checkAddButton implements Runnable
    {
        boolean ret=true;
        @Override
        public void run() {
            if(Utils.getInstance(WeatherScreen.this).checkIfContainsCity(data[5],data[8]))
            {
                ret=false;
            }
            else {
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance(WeatherScreen.this);
                        if (Utils.addCity(new City(data[6], data[7], data[5],data[8]))) {
                            if(!(WeatherScreen.this).isFinishing())
                            {
                                Toast.makeText(WeatherScreen.this, data[5] + " Added to Saved Cities", Toast.LENGTH_SHORT).show();
                            }
                            addButton.setEnabled(false);
                        }
                    }
                });
            }
        }
        public boolean getRet()
        {
            return ret;
        }
    }
}