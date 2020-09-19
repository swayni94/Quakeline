package com.example.quakeline.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class QuakelinesBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //get the broadcast message
        Log.i("sendbroadcast32", "send broadcast32");
        int resultCode=intent.getIntExtra("resultCode",RESULT_CANCELED);
        if (resultCode == RESULT_OK) {
            String message = intent.getStringExtra("toastMessage");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.i("sendbroadcast55", message);
        }
    }
}
