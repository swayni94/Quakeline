package com.example.quakeline.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quakeline.R;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.ViewPage.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment {

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mapViewModel.getNewQuakes().observe(getViewLifecycleOwner() , results -> {
                for (Result result : results) {
                    LatLng x = new LatLng(result.getLat() , result.getLng());
                    googleMap.addCircle(new CircleOptions().clickable(false).strokeColor(0).center(x).radius(radius).fillColor(0)).setFillColor(R.color.softcolor_red);
                    googleMap.addMarker(new MarkerOptions().position(x).title(result.getTitle()).snippet(result.getMag() + " büyüklüğünde " + result.getDepth() + " km derinliğinde"));
                }
            });
            mapViewModel.getLocation().observe(getViewLifecycleOwner() , location -> {
                Log.e("MapFragment" , "Location is running");
                LatLng x = new LatLng(location.getLatitude() , location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x , 5));
                googleMap.addCircle(new CircleOptions().center(x).radius(radius).strokeColor(0).fillColor(1)).setFillColor(R.color.softcolor_blue);
                googleMap.addMarker(new MarkerOptions().position(x).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater ,
                             @Nullable ViewGroup container ,
                             @Nullable Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return inflater.inflate(R.layout.fragment_maps , container , false);
    }

    private MainViewModel mapViewModel;
    double radius = 10000;

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps_fragment);

        if (mapFragment!=null) {
            mapFragment.getMapAsync(callback);
        }
    }
}