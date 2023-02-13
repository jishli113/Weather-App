package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchScreen extends AppCompatActivity{
private static SearchView cityInput;
private static ProgressBar progressBar;
private static ImageView backButton;
private static ListView listView;
private static SearchListViewAdapter adapter;
private Context context=SearchScreen.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        initViews();
    }
    public void initViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cityInput=findViewById(R.id.searchView);
        progressBar=findViewById(R.id.progressBar);
        backButton=findViewById(R.id.search_screen_back_button);
        listView=findViewById(R.id.listView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter=new SearchListViewAdapter(SearchScreen.this,R.layout.saved_cities_item,Utils.getInstance(context).getAllCities());
        listView.setAdapter(adapter);
        cityInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<City> cities=new ArrayList<City>();
                for(City city:Utils.getInstance(context).getAllCities())
                {
                    if(city.getName().toLowerCase().contains(newText.toLowerCase()))
                    {
                        cities.add(city);
                    }
                }
                SearchListViewAdapter adapter=new SearchListViewAdapter(context,R.layout.saved_cities_item,cities);
                listView.setAdapter(adapter);
                return false;
            }
        });
    }
    public class FetchCityInfo extends AsyncTask<City,Integer,String[]> {
        @Override
        protected String[] doInBackground(City... cities) {
            try {
                return Utils.getInstance(context).getCityWeatherInfoSavedCities(cities[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            Intent intent=new Intent(context, WeatherScreen.class);
            intent.putExtra("SEARCH_SCREEN",strings);
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
    public class SearchListViewAdapter extends ArrayAdapter<City> {
        Context context;
        int resource;
        public SearchListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context=context;
            this.resource=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String city=getItem(position).getName();
            String region=getItem(position).getRegion();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(resource, parent, false);
            TextView cityName=(TextView)convertView.findViewById(R.id.savedCityName);
            TextView regionName=(TextView)convertView.findViewById(R.id.savedCityRegion);
            CardView cardView=(CardView)convertView.findViewById(R.id.savedItemCardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FetchCityInfo asyncTask=new FetchCityInfo();
                    asyncTask.execute(getItem(position));
                }
            });
            cityName.setText(city);
            regionName.setText(region);
            return convertView;
        }
    }

}