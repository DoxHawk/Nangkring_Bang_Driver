package com.example.nangkringbangdriver.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nangkringbangdriver.databinding.ActivitySplashBinding;

public class Activity_Splash extends AppCompatActivity {

    private ActivitySplashBinding main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                =   ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Activity_Splash.this.startActivity(new Intent(Activity_Splash.this, Activity_Index.class));
                Activity_Splash.this.finish();
            }
        },(1 * 1000));
    }
}
