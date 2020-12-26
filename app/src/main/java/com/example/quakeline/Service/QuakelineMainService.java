package com.example.quakeline.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.quakeline.Helpers.CurrentLocationHelper;
import com.example.quakeline.Helpers.MainLocationListener;
import com.example.quakeline.R;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.RestApi.Repository;
import com.example.quakeline.ViewPage.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class QuakelineMainService extends JobIntentService implements MainLocationListener.IUserLocationListener {
    private static final int JOB_ID = 1;
    private Repository repository;

    private static final String TAG = JobService.class.getSimpleName();

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, QuakelineMainService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate is start");
        repository = Repository.getInstance();
        MainLocationListener mainLocationListener = MainLocationListener.getInstance();
        mainLocationListener.startListiningUserLocation(getApplicationContext(), this);
        currentLocationHelper = new CurrentLocationHelper();
    }

    @Override
    public IBinder onBind(@NotNull Intent intent) {
        return null;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
        setRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private CurrentLocationHelper currentLocationHelper;
    private Location _location;

    private void setRequest()
    {
        ArrayList<Result> results = repository.getQuakeRequestBackground();
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<Result> nearQuakes = new ArrayList<Result>();
        for (Result result : results){
            Log.e(TAG, "Item is "+result.getTitle());
            notificationQuakeline("item is"+result.getTitle());
            if (currentLocationHelper.currentLocation(_location.getLatitude(),_location.getLongitude(), result.getLat(),result.getLng(),50))
            {
                nearQuakes.add(result);
                String term = " "+result.getTitle() + " - " + result.getDate() + " - " + result.getMag();
                notificationQuakeline(term);
            }
        }
    }

    public void notificationQuakeline(String quakelineNotificationDetail)
    {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Quekeline Service")
                .setContentText(quakelineNotificationDetail)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        int i = new Random().nextInt(9999-1000);
        managerCompat.notify(i, notification);
    }

    public static final String CHANNEL_ID = "QuekelineServiceChannel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Quekeline Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onLocationChange(Location location) {
        this._location = location;
        Log.e("MainService", "Listener is successful");
    }
}