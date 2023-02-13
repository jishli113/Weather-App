package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView txtSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        recyclerView=findViewById(R.id.recyclerView);
        txtSettings=findViewById(R.id.txtSettings);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        Context context;
        String[] types={"Styles","Weather Info"};
        int[] icons={R.drawable.ic_preferences,R.drawable.ic_filter};
        Intent[] intents;
        public RecyclerViewAdapter(Context context)
        {
            this.context=context;
            intents= new Intent[]{new Intent(context, DesignPreferences.class), new Intent(context, WeatherInfoPreferences.class)};
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(this.context).inflate(R.layout.settings_item,parent, false);
            ViewHolder vh=new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.type.setText(types[position]);
            holder.icon.setBackgroundResource(icons[position]);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intents[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            CardView parent;
            TextView type;
            ImageView icon;
            ImageView arrow;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
               parent= itemView.findViewById(R.id.settingsCardView);
               type=itemView.findViewById(R.id.settingType);
               icon=itemView.findViewById(R.id.icon);
               arrow=itemView.findViewById(R.id.forwardArrow);
            }
        }
    }
}