package com.example.quakeline.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.quakeline.Service.QuakelineService;

public class ServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, QuakelineService.class);
        context.startService(serviceIntent);
    }
}
