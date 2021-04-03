package com.example.quakeline.ViewPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.quakeline.R;
import com.example.quakeline.Receivers.ServiceBroadcastReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

   // private QuakelinesBroadcastReceiver quakelinesBroadcastReceiver;

    MainViewModel mainViewModel;

    private NavController navController;

    Toolbar toolbar;
    private boolean floatingActionMap = false;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
        }

        mainViewModel = new  ViewModelProvider(this).get(MainViewModel.class);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.main_floating);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.main_fragment);
        floatingActionButton.setOnClickListener(this);

/*
        quakelinesBroadcastReceiver = new QuakelinesBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QuakelineService.ACTION);
        registerReceiver(quakelinesBroadcastReceiver , intentFilter);
*/
        scheduleAlarm();
    }

    private void scheduleAlarm()
    {
        Intent intent = new Intent(getApplicationContext(), ServiceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
        long starttime=System.currentTimeMillis();
        AlarmManager backupAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(backupAlarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, starttime, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QuakelineService.ACTION);
        registerReceiver(quakelinesBroadcastReceiver, intentFilter);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(quakelinesBroadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
       // mainViewModel.locationRemoveUpdetes();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (floatingActionMap) {
            navController.navigate(R.id.quake_list_fragment);
            floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_action_name));
            floatingActionMap = false;
        }
        else {
            navController.navigate(R.id.maps_fragment);
            floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_listpage_name));
            floatingActionMap = true;
        }
    }
}
