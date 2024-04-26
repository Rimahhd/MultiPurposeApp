package com.example.multipurposeapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    String TAG = "Contacts Activity";

    RecyclerView contactRV;
    ArrayList<ContactsModel> arrayList = new ArrayList<ContactsModel>();
    ContactsAdapter adapter;
    ImageView weatherNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts);
        Log.i(TAG, "Contacts Screen Created.");

        contactRV = findViewById(R.id.idRVContact);
        weatherNav = findViewById(R.id.idIVWeatherNav);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //Night Mode On
                weatherNav.setImageResource(R.drawable.img_clouds_white);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                //Night Mode Off
                weatherNav.setImageResource(R.drawable.img_clouds_black);
                break;
        }

        weatherNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherIntent = new Intent(ContactsActivity.this, WeatherActivity.class);
                startActivity(weatherIntent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        checkPermission();
    }

    private void checkPermission() {

        if(ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }else{
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+ " ASC";
        Cursor cursor = getContentResolver().query(uri, null, null, null, sort);

        if(cursor.getCount() > 0){

            while(cursor.moveToNext()){
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);

                if(phoneCursor.moveToNext()){

                    @SuppressLint("Range")
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ContactsModel model = new ContactsModel();

                    model.setName(name);
                    model.setNumber(number);
                    arrayList.add(model);
                    phoneCursor.close();

                }
            }

            cursor.close();
        }

        contactRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(this, arrayList);
        contactRV.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            getContactList();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Contacts Screen Started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Contacts Screen Resumed.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Contacts Screen Paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Contacts Screen Stopped.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Contacts Screen Restarted.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Contacts Screen Destroyed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Back Button Pressed in Contacts Screen.");

        Intent dashIntent = new Intent(ContactsActivity.this, WeatherActivity.class);
        startActivity(dashIntent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "Contacts Screen Finished.");
    }

    @Override
    public boolean isFinishing() {
        super.isFinishing();
        Log.i(TAG, "Contacts Screen is Finishing.");
        return true;
    }
}
