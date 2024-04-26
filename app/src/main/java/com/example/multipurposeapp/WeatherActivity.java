package com.example.multipurposeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.kwabenaberko.openweathermaplib.constant.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.implementation.callback.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    String TAG = "Weather Activity";

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, countryNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, searchIV, mailNav, contactNav, conditionIV;
//    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
//    private WeatherRVAdapter weatherRVAdapter;
    private int PERMISSION_CODE = 1;
    private String cityName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));

        Log.i(TAG, "Weather Screen Created");
        setContentView(R.layout.layout_weather);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        countryNameTV = findViewById(R.id.idTVCountryName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRvWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        conditionIV = findViewById(R.id.idIVCondition);
        searchIV = findViewById(R.id.idIVSearch);
        mailNav = findViewById(R.id.idIVMailNav);
        contactNav = findViewById(R.id.idIVContactNav);
//        weatherRVModelArrayList = new ArrayList<>();
//        weatherRVAdapter = new WeatherRVAdapter(this.weatherRVModelArrayList);
//        weatherRV.setAdapter(weatherRVAdapter);
        cityName = "Current Location";

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //Night Mode On
                mailNav.setImageResource(R.drawable.img_mail_white);
                contactNav.setImageResource(R.drawable.img_contact_white);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                //Night Mode Off
                mailNav.setImageResource(R.drawable.img_mail_black);
                contactNav.setImageResource(R.drawable.img_contact_black);
                break;
        }

        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        Log.i(TAG, "Time is " +hour);
        if (hour >= 6 && hour < 18) {
            // Daytime
            backIV.setImageResource(R.drawable.day);
        } else {
            // Nighttime
            backIV.setImageResource(R.drawable.night);
        }

        mailNav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(WeatherActivity.this, MailActivity.class);
                startActivity(mailIntent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        contactNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(WeatherActivity.this, ContactsActivity.class);
                startActivity(contactIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        getWeatherOnLoad();
        cityNameTV.setText(cityName);

        searchIV.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String city = cityEdt.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(WeatherActivity.this, "Please enter the city name.", Toast.LENGTH_SHORT).show();
                }else{
                    cityNameTV.setText(city);
                    getWeatherInfo(city);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please provide permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private double getLongitude(){
        double longitude;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        longitude = location.getLongitude();

        return longitude;
    }

    private double getLatitude(){
        double latitude;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        latitude = location.getLatitude();

        return latitude;
    }

    private void getWeatherOnLoad(){

        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC);

        double latitude = getLatitude();
        double longitude = getLongitude();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                countryNameTV.setText(address.getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //cityNameTV.setText(cityName);

        helper.getCurrentWeatherByGeoCoordinates(latitude, longitude, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {

                double temperature = currentWeather.getMain().getTempMax();
                temperatureTV.setText(temperature + "°C");
                String condition = currentWeather.getWeather().get(0).getDescription();
                conditionTV.setText(condition);
                String conditionImg = ("http://openweathermap.org/img/wn/" + currentWeather.getWeather().get(0).getIcon() + "@2x.png");
                Glide.with(WeatherActivity.this)
                        .load(conditionImg)
                        .dontAnimate()
                        .into(conditionIV);

                cityNameTV.setText(currentWeather.getName());

                //Logging in case of any errors
                /*Log.i(TAG, "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLon() +"\n"
                        +"Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                        +"Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                        +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                        +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );*/
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
            }
        });

    }
    private void getWeatherInfo(String cityName){

        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC);

        helper.getCurrentWeatherByCityName(cityName, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {

                double temperature = currentWeather.getMain().getTempMax();
                temperatureTV.setText(temperature + "°C");
                String condition = currentWeather.getWeather().get(0).getDescription();
                conditionTV.setText(condition);
                String conditionImg = ("http://openweathermap.org/img/wn/" + currentWeather.getWeather().get(0).getIcon() + "@2x.png");
                Glide.with(WeatherActivity.this)
                        .load(conditionImg)
                        .dontAnimate()
                        .into(conditionIV);

                //Logging in case of any errors
                /*Log.i(TAG, "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLon() +"\n"
                        +"Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                        +"Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                        +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                        +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );*/
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
            }
        });

        helper.getThreeHourForecastByCityName(cityName, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {

//                weatherRVModelArrayList.clear();

                //Implement a 3 hour forecast over 5 days, extends recycler view and adds data to item 30/1/2023
                //Might not have time to finish it

                //Logging in case of any errors / Currently gets an array of size 40
                Log.i(TAG, "City/Country: "+ threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() +"\n"
                        +"Forecast Array Count: " + threeHourForecast.getCnt() +"\n"
                        //For this example, we are logging details of only the first forecast object in the forecasts array
                        +"First Forecast Date Timestamp: " + threeHourForecast.getList().get(0).getDt() +"\n"
                        +"First Forecast Weather Description: " + threeHourForecast.getList().get(0).getWeather().get(0).getDescription()+ "\n"
                        +"First Forecast Max Temperature: " + threeHourForecast.getList().get(0).getMain().getTempMax()+"\n"
                        +"First Forecast Wind Speed: " + threeHourForecast.getList().get(0).getWind().getSpeed() + "\n"
                );
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v(TAG, throwable.getMessage());
            }
        });


        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherRVModelArrayList.clear();
                try{

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecast0.getJSONArray("hour");

                    for(int i=0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherRVModelArrayList.add(new WeatherRVModel(time, temper, img, wind));

                    }
                    weatherRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "Please enter a valid city name.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Weather Screen Started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Weather Screen Resumed.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Weather Screen Paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Weather Screen Stopped.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Weather Screen Restarted.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Weather Screen Destroyed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Back Button Pressed in Weather Screen.");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "Weather Screen Finished.");
    }

    @Override
    public boolean isFinishing() {
        super.isFinishing();
        Log.i(TAG, "Weather Screen is Finishing.");
        return true;
    }

}



