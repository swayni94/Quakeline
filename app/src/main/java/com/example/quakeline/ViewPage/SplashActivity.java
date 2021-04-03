package com.example.quakeline.ViewPage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.quakeline.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity implements LifecycleOwner {

    private boolean term = false;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this , Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) this , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 1);
            } else {
                ActivityCompat.requestPermissions((Activity) this , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 1);
            }
        }

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        textView = findViewById(R.id.splash_textview);

        listenerLocation(mainViewModel, this);
    }

    private void listenerLocation(MainViewModel mainViewModel, LifecycleOwner owner){
        CountDownTimer timer = new CountDownTimer(3000 , 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                mainViewModel.getLocation().observe(owner, location -> {
                    if (location !=null){
                        term = true;
                    }
                });
                if (term) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    textView.setText("Location is failed. => \n \n Please wait!");
                    listenerLocation(mainViewModel, owner);
                }
            }
        };
        timer.start();
    }
}