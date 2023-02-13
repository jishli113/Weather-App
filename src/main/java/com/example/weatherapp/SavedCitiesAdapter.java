package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SavedCitiesAdapter extends RecyclerView.Adapter<SavedCitiesAdapter.ViewHolder>{
    private static ArrayList<City> cities;
    private static Context context;
    public SavedCitiesAdapter(Context context)
    {
        cities=Utils.getInstance(context).getSavedCities();
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_cities_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.savedCityName.setText(cities.get(position).getName());
        holder.savedCityRegion.setText(cities.get(position).getRegion());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class Runn implements Runnable
                {
                    Context mContext;
                    City city;
                    String[] data;
                    public Runn(Context context, City city)
                    {
                        mContext=context;
                        this.city=city;
                    }
                    @Override
                    public void run() {
                        try {
                            data=Utils.getInstance(mContext).getCityWeatherInfoSavedCities(city);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    public String[] getData() {
                        return data;
                    }
                }
               // while(cities.get(position).ifDataNull());
                Intent intent=new Intent(context,WeatherScreen.class);
                Runn runnable=new Runn(context,cities.get(position));
                Thread thread=new Thread(runnable);
                thread.start();
                while(runnable.getData()==null);
                    intent.putExtra("SAVED_CITIES",runnable.getData());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
    public void setcities(ArrayList<City> cities)
    {
        this.cities=cities;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CardView cardView;
        private TextView savedCityName;
        private TextView savedCityRegion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.savedItemCardView);
            savedCityName=itemView.findViewById(R.id.savedCityName);
            savedCityRegion=itemView.findViewById(R.id.savedCityRegion);
        }
    }
}
