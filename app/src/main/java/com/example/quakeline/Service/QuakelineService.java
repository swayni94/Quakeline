package com.example.quakeline.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.example.quakeline.R;
import com.example.quakeline.RestApi.IRestServise;
import com.example.quakeline.RestApi.Model.QuakeResponseModel;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.ViewPage.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class QuakelineService extends IntentService {

    public static final String ACTION = "com.example.quakeline.Receivers.QuakelinesBroadcastReceiver";

    public QuakelineService() {
        super("QuakelineService");
    }

    private static final String TAG = "QUAKELINETESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private class LocationListener implements android.location.LocationListener{
        Location mLocation;

        private LocationListener(String provider){
            Log.e(TAG, "LocationListener " + provider);
            mLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {
            Log.e(TAG,"onStatusChanged: " +provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG,"onProviderEnabled: " +provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG,"onProviderDisabled: " +provider);
        }

        Location getmLocation() {
            return mLocation;
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
                    new LocationListener(LocationManager.GPS_PROVIDER),
                    new LocationListener(LocationManager.NETWORK_PROVIDER),
                    new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void getUserLocation()
    {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[2]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "passive provider does not exist " + ex.getMessage());
        }
    }

    private void locationRemoveUpdetes()
    {
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void setRequest()
    {
        String baseURL = "https://api.orhanaydogdu.com.tr";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        IRestServise servise = retrofit.create(IRestServise.class);

        servise.getQuakes().enqueue(new Callback<QuakeResponseModel>() {
            @Override
            public void onResponse(Call<QuakeResponseModel> call, Response<QuakeResponseModel> response) {
                if (response.isSuccessful()){
                    Location location = mLocationListeners[2].getmLocation();
                    Log.e(TAG, " "+location.getLatitude()+ " - " + location.getLongitude());
                    //notificationQuakeline("2- "+location.getLatitude()+ " - " + location.getLongitude());//Deneme!!
                    List<Result> results = Objects.requireNonNull(response.body()).getResult();
                    List<Result> nearQuakes = new ArrayList<>();
                    for (int i=0; i<results.size();i++){
                        double x= results.get(i).getLat();
                        double y= results.get(i).getLng();
                        if (currentLocation(location.getLatitude(),location.getLongitude(), x,y))
                        {
                            nearQuakes.add(results.get(i));
                            String term = " "+results.get(i).getTitle() + " - " + results.get(i).getDate() + " - " + results.get(i).getMag();
                            notificationQuakeline(term);
 //Düzenlenecek!!                           //
                        }
                    }
                }
            }

            @SuppressLint("ShowToast")
            @Override
            public void onFailure(Call<QuakeResponseModel> call, Throwable t) {
                Toast.makeText(getApplication(),"error", Toast.LENGTH_LONG);
            }
        });
    }

    private boolean currentLocation(double userlat, double userlon, double quakelat, double quakelon)
    {
        int kmSabiti=1000;
        int km=50;
        Location startPoint=new Location("locationUser");
        startPoint.setLatitude(userlat);
        startPoint.setLongitude(userlon);

        Location endPoint=new Location("quakeline");
        endPoint.setLatitude(quakelat);
        endPoint.setLongitude(quakelon);

        double distance=startPoint.distanceTo(endPoint);
//Düzenlenecek!!
        distance = distance / kmSabiti;
        return distance< km;
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

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, notification);
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("QuekelineBackgroundService","Quekeline Service Running!");
        setRequest();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "create");
        getUserLocation();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"destroy");
        super.onDestroy();
        locationRemoveUpdetes();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
