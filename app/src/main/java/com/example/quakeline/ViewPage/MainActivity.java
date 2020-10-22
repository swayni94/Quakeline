package com.example.quakeline.ViewPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.quakeline.Adapters.QuakelineRecyclerViewAdapter;
import com.example.quakeline.R;
import com.example.quakeline.Receivers.QuakelinesBroadcastReceiver;
import com.example.quakeline.Receivers.ServiceBroadcastReceiver;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.Service.QuakelineService;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements LifecycleOwner{

    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    QuakelinesBroadcastReceiver quakelinesBroadcastReceiver;

    RecyclerView recyclerView;
    QuakelineRecyclerViewAdapter recyclerViewAdapter;

    MainViewModel mainViewModel;
    MainActivity context=this;

    LocationManager mLocationManager;
    Location userLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        assert mLocationManager != null;
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 10f, mLocationListener);

        recyclerView = findViewById(R.id.quakeRecyclerview);
       // mainViewModel = ViewModelProviders.of(context).get(MainViewModel.class);


        quakelinesBroadcastReceiver = new QuakelinesBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QuakelineService.ACTION);
        registerReceiver(quakelinesBroadcastReceiver , intentFilter);

        scheduleAlarm();
    }

    private void scheduleAlarm()
    {
        Intent intent = new Intent(getApplicationContext(), ServiceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
        long starttime=System.currentTimeMillis();
        AlarmManager backupAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(backupAlarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, starttime, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QuakelineService.ACTION);
        registerReceiver(quakelinesBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(quakelinesBroadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    final Observer<List<Result>> resultListUpdateObserver = resultList -> {
        Log.e("geciciSorgu2", "deneme2");
        if (userLocation.getLatitude()>0 && resultList.size()>0) {
            recyclerViewAdapter = new QuakelineRecyclerViewAdapter(resultList , context , userLocation.getLatitude() , userLocation.getLongitude());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    };


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.e("geciciSorgu", "deneme");
            userLocation = location;
            mainViewModel = new  ViewModelProvider(context).get(MainViewModel.class);
            mainViewModel.getNewQuakes().observe(context , resultListUpdateObserver);
        }

        @Override
        public void onStatusChanged(String s , int i , Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
