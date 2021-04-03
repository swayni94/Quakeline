package com.example.watch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.common.RestApi.Model.Result;
import com.example.common.helper.QuakelineItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.SwipeDismissFrameLayout;

public class MapsActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider,
        OnMapReadyCallback,GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    private SupportMapFragment mapFragment;
    private MapActivityViewModel mainViewModel;
    double radius = 10000;
    private ClusterManager<QuakelineItem> clusterManager;

    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        AmbientModeSupport.AmbientController controller = AmbientModeSupport.attach(this);
        boolean isAmbient = controller.isAmbient();
        setContentView(R.layout.activity_maps);

        if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
        }

        final SwipeDismissFrameLayout swipeDismissRootFrameLayout =
                (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root_container);

        swipeDismissRootFrameLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                layout.setVisibility(View.GONE);
                finish();
            }
        });

        swipeDismissRootFrameLayout.setOnApplyWindowInsetsListener(

                (view , insets) -> {
                    insets = swipeDismissRootFrameLayout.onApplyWindowInsets(insets);

                    return insets;
                });

        mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mainViewModel = new ViewModelProvider(this).get(MapActivityViewModel.class);
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        mainViewModel.getLocation().observe(this , location -> {
            Log.e("MapFragment" , "Location is running");
            LatLng x = new LatLng(location.getLatitude() , location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x , 5));
        });
        clusterManager = new ClusterManager<>(this, googleMap);
        mainViewModel.getNewQuakes().observe(this , results -> {
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
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new AmbientModeSupport.AmbientCallback() {
            public void onEnterAmbient(Bundle ambientDetails) {
                super.onEnterAmbient(ambientDetails);
                mapFragment.onEnterAmbient(ambientDetails);
            }
            public void onExitAmbient() {
                super.onExitAmbient();
                mapFragment.onExitAmbient();
            }
        };
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