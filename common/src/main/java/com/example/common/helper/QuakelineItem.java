package com.example.common.helper;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QuakelineItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;

    public QuakelineItem(double lat, double lng, String title, String snippet){
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
    }
    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }
}
