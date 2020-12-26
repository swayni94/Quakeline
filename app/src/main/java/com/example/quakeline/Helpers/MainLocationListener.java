package com.example.quakeline.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainLocationListener {

    private static MainLocationListener mainLocationListener;

    public static MainLocationListener getInstance(){
        if (mainLocationListener == null)
        {
            mainLocationListener = new MainLocationListener();
        }
        return mainLocationListener;
    }

    public void startListiningUserLocation(Context context, IUserLocationListener listener){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.e("MainLocationListener", "listener listen location");
        LocationListener locationListener = listener::onLocationChange;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_COARSE_LOCATION )){
                ActivityCompat.requestPermissions((Activity) context , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 1);
            }
            else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        else {
            int LOCATION_REFRESH_TIME = 10000;
            int LOCATION_REFRESH_DISTANCE = 30;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME ,
                    LOCATION_REFRESH_DISTANCE ,
                    locationListener);
        }
    }

    public interface IUserLocationListener{
        void onLocationChange(Location location);
    }
}
