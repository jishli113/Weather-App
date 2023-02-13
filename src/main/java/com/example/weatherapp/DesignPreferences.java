package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DesignPreferences extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
private RecyclerView recyclerView;
private Spinner spinner;
private Button btnReset;
private Button btnConfirm;
private String selected="Primary Theme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_preferences);
        recyclerView=findViewById(R.id.rcDesignPreference);
        spinner=findViewById(R.id.dpSpinner);
        btnReset=findViewById(R.id.btnReset);
        btnConfirm=findViewById(R.id.btnConfirm);
        ColorPickerRecyclerViewAdapter adapter=new ColorPickerRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager=new GridLayoutManager(this,4,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        ArrayAdapter<CharSequence> spinnerAdapter=ArrayAdapter.createFromResource(this, R.array.design_preferences_items,R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!parent.getItemAtPosition(position).toString().equals(selected))
        {
            selected=parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ColorPickerRecyclerViewAdapter extends RecyclerView.Adapter<ColorPickerRecyclerViewAdapter.ViewHolder>
    {
        private Context mContext;
        private int[] colorResources={R.color.red,R.color.dark_orange,R.color.orange,R.color.yellow,R.color.green,R.color.teal_200,R.color.teal_700,R.color.blue,R.color.purple_200,R.color.purple_500,R.color.purple_700,R.color.brown,R.color.cream_brown,R.color.cream,R.color.pink,R.color.dark_pink,R.color.magenta, R.color.grey1,R.color.grey2};
        private int pickedColor;
        public ColorPickerRecyclerViewAdapter(Context context)
        {
            mContext=context;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.color_picker_item,parent,false);
            ViewHolder vh=new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.button.setBackgroundColor(getResources().getColor(colorResources[position]));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickedColor=colorResources[position];
                }
            });
        }


        public int getColor()
        {
            return pickedColor;
        }


        @Override
        public int getItemCount() {
            return colorResources.length;
        }
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private Button button;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                button=itemView.findViewById(R.id.colorButton);
            }
        }

    }
}
