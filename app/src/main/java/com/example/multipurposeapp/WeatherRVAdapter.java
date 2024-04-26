/*
package com.example.multipurposeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModel> forecastList;

    public WeatherRVAdapter(ArrayList<WeatherRVModel> forecastList) {
        this.forecastList = forecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_weather_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the forecast at the specified position

        WeatherRVModel forecast = forecastList.get(position);
        holder.temperateTV.setText(forecast.getTemperature() + "°C");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh aa");
        try{
            Date t = input.parse(forecast.getTime());
            holder.timeTV.setText((output.format(t)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.icon.setImageResource(forecast.getIcon());

        // bind the forecast data to the view elements
    }

    private int getIcon() {
        return 0;
    }


    private String getTemperature() {
        return null;
    }

    private String getTime() {
        return null;
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        // define the view elements
        private TextView temperateTV, timeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperateTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idTVCondition);

        }
    }
}
*/
