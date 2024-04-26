package com.example.multipurposeapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailActivity extends AppCompatActivity {

    String TAG = "Mail Activity";
    EditText EdtTo, EdtSubject, EdtMessage;
    Button sendBtn;
    String sEmail, sPassword;
    ImageView weatherNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mail);

        EdtTo = findViewById(R.id.idEdtTo);
        EdtSubject = findViewById(R.id.idEdtSubject);
        EdtMessage = findViewById(R.id.idEdtMessage);
        sendBtn = findViewById(R.id.idBtnSend);
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
                Intent weatherIntent = new Intent(MailActivity.this, WeatherActivity.class);
                startActivity(weatherIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        sEmail = "noreply.souheil@gmail.com";
        //sPassword = "2P._CRd75Rz.cdw";
        sPassword = "gubxhtpljpnvycbe";

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sEmail, sPassword);
                    }
                });

                try {
                    Message message = new MimeMessage(session);

                    message.setFrom(new InternetAddress(sEmail));

                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EdtTo.getText().toString().trim()));

                    message.setSubject(EdtSubject.getText().toString().trim());

                    message.setText(EdtMessage.getText().toString().trim());

                    new SendMail().execute(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MailActivity.this,"Please Wait", "Sending Mail", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                Log.i(TAG, "Transport message: " + messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.i(TAG, "Messaging Error: " + e);
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if(s.equals("Success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(MailActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'> Success </font>"));
                builder.setMessage("Mail sent successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        EdtTo.setText("");
                        EdtSubject.setText("");
                        EdtMessage.setText("");
                    }
                });

                builder.show();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Mail Screen Started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Mail Screen Resumed.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Mail Screen Paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Mail Screen Stopped.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Mail Screen Restarted.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Mail Screen Destroyed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "Back Button Pressed in Mail Screen.");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "Mail Screen Finished.");
    }

    @Override
    public boolean isFinishing() {
        super.isFinishing();
        Log.i(TAG, "Mail Screen is Finishing.");
        return true;
    }
}
