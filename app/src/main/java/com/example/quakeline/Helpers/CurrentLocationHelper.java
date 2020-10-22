package com.example.quakeline.Helpers;

import android.location.Location;

public class CurrentLocationHelper {

    public boolean currentLocation(double userlat, double userlon, double quakelat, double quakelon, int km)
    {
        int kmSabiti=1000;
        Location startPoint=new Location("locationUser");
        startPoint.setLatitude(userlat);
        startPoint.setLongitude(userlon);

        Location endPoint=new Location("quakeline");
        endPoint.setLatitude(quakelat);
        endPoint.setLongitude(quakelon);

        double distance=startPoint.distanceTo(endPoint);

        distance = distance / kmSabiti;
        return distance < km;
    }


    public double currentLocationKm(double userlat, double userlon, double quakelat, double quakelon)
    {
        int kmSabiti=1000;
        Location startPoint=new Location("locationUser");
        startPoint.setLatitude(userlat);
        startPoint.setLongitude(userlon);

        Location endPoint=new Location("quakeline");
        endPoint.setLatitude(quakelat);
        endPoint.setLongitude(quakelon);

        double distance=startPoint.distanceTo(endPoint);

        distance = distance / kmSabiti;
        return distance;
    }
}
