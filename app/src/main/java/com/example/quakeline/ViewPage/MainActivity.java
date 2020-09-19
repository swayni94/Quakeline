package com.example.quakeline.ViewPage;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.quakeline.R;
import com.example.quakeline.Receivers.QuakelinesBroadcastReceiver;
import com.example.quakeline.Receivers.ServiceBroadcastReceiver;
import com.example.quakeline.Service.QuakelineService;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    QuakelinesBroadcastReceiver quakelinesBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quakelinesBroadcastReceiver = new QuakelinesBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QuakelineService.ACTION);
        registerReceiver(quakelinesBroadcastReceiver, intentFilter);

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
}
