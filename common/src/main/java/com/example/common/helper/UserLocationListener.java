package com.example.common.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UserLocationListener {
    private static UserLocationListener userLocationListener;

    public static UserLocationListener getInstance() {
        if (userLocationListener == null) {
            userLocationListener = new UserLocationListener();
        }
        return userLocationListener;
    }

    public void startListeningUserLocation(Context context, IUserLocationListener listener ) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ContextCompat.checkSelfPermission(context , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context , Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) context , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 1);
            } else {
                ActivityCompat.requestPermissions((Activity) context , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION} , 1);
            }
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(listener::onLocationChange);
        }
    }

    public interface IUserLocationListener{
        void onLocationChange(Location location);
    }
}
