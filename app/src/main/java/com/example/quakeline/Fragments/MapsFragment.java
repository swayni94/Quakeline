package com.example.quakeline.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.RestApi.Model.Result;
import com.example.common.helper.QuakelineItem;
import com.example.quakeline.R;

import com.example.quakeline.ViewPage.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

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
    private ClusterManager<QuakelineItem> clusterManager;
    private Context context;

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps_fragment);
        context = getActivity();
        if (mapFragment!=null) {
            mapFragment.getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
         mapViewModel.getLocation().observe(getViewLifecycleOwner() , location -> {
            Log.e("MapFragment" , "Location is running");
            LatLng x = new LatLng(location.getLatitude() , location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x , 5));
        });
        clusterManager = new ClusterManager<>(context, googleMap);
        mapViewModel.getNewQuakes().observe(getViewLifecycleOwner() , results -> {
            for (Result result : results) {
                LatLng x = new LatLng(result.getLat() , result.getLng());
                googleMap.addCircle(new CircleOptions().clickable(false).strokeColor(0).center(x).radius(radius).fillColor(0)).setFillColor(R.color.softcolor_red);
                clusterManager.addItem(new QuakelineItem(result.getLat(), result.getLng(), result.getTitle(),
                        (result.getMag() + " büyüklüğünde " + result.getDepth() + " km derinliğinde")));
            }
        });
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onCameraIdle() {
        clusterManager.onCameraIdle();
    }

}

