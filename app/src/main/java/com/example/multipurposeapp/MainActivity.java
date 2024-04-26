package com.example.multipurposeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Login Screen Created.");
        setContentView(R.layout.layout_login);

        Button loginButton = findViewById(R.id.Login_BtnLogin);
        TextView UsernameTextView = findViewById(R.id.Login_Username);
        TextView PasswordTextView = findViewById(R.id.Login_Password);
        UsernameTextView.setFocusable(true);
        UsernameTextView.requestFocus();
        PasswordTextView.setFocusable(true);


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkLoginInfo();
            }
        });

        //User pressed 'OK' in Username
        UsernameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if((keyEvent != null && (keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) || i == EditorInfo.IME_ACTION_DONE){
                    Log.i(TAG, "Enter Key Pressed in Username.");
                    PasswordTextView.requestFocus();
                }
                return false;
            }
        });

        //User pressed 'OK' in Password
        PasswordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if((keyEvent != null && (keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) || i == EditorInfo.IME_ACTION_DONE){
                    Log.i(TAG, "Enter Key Pressed in Password.");
                    checkLoginInfo();
                }
                return false;
            }
        });
    }

    public void checkLoginInfo() {

        TextView UsernameTextView = findViewById(R.id.Login_Username);
        TextView PasswordTextView = findViewById(R.id.Login_Password);
        String username = UsernameTextView.getText().toString();
        String password = PasswordTextView.getText().toString();

        if (username.equals("") && password.equals("")) {
            // Login success

            Intent dashIntent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(dashIntent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            // Login failed
            LoginFailedAlert();
        }
    }

    private void LoginFailedAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Login Failed!");
        builder.setMessage("Please check your username or password.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Log.i(TAG, "Login Failed Alert Created.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Login Screen Started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Login Screen Resumed.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Login Screen Paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Login Screen Stopped.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Login Screen Restarted.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Login Screen Destroyed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Back Button Pressed in Login Screen.");
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "Login Screen Finished.");
    }

    @Override
    public boolean isFinishing() {
        super.isFinishing();
        Log.i(TAG, "Login Screen is Finishing.");
        return true;
    }
}