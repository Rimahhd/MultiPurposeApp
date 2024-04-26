package com.example.multipurposeapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kwabenaberko.openweathermaplib.model.common.Main;

public class DashboardActivity extends AppCompatActivity {

    String Tag = "Dashboard Activity";

    private int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Dashboard Screen Created.");
        setContentView(R.layout.layout_dashboard);

        if(ContextCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        if(ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Button weatherButton = findViewById(R.id.dash_WeatherBtn);
        weatherButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent weatherIntent = new Intent(DashboardActivity.this, WeatherActivity.class);
                startActivity(weatherIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        Button contactsButton = findViewById(R.id.dash_ContactsBtn);
        contactsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(DashboardActivity.this, ContactsActivity.class);
                startActivity(contactIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        Button mailButton = findViewById(R.id.dash_MailBtn);
        mailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(DashboardActivity.this, MailActivity.class);
                startActivity(mailIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Dashboard Screen Started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Dashboard Screen Resumed.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Dashboard Screen Paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Dashboard Screen Stopped.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Dashboard Screen Restarted.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Dashboard Screen Destroyed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Back Button Pressed in Dashboard Screen.");
        Intent loginIntent = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "Dashboard Screen Finished.");
    }

    @Override
    public boolean isFinishing() {
        super.isFinishing();
        Log.i(TAG, "Dashboard Screen is Finishing.");
        return true;
    }
}
