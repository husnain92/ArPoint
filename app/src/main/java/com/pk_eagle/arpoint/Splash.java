package com.pk_eagle.arpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        splashtime();



    }
    public void splashtime() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(UtilSharedPref.getStringVal(getApplicationContext(), UtilSharedPref.USERID).equals("")) {
                    Intent i = new Intent(Splash.this, Activity_Login.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, 2000);
    }
}
