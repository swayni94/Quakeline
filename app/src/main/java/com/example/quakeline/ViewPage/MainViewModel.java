package com.example.quakeline.ViewPage;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.example.quakeline.Helpers.MainLocationListener;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.RestApi.Repository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel implements MainLocationListener.IUserLocationListener {

    private MutableLiveData<List<Result>> quakeResponseModelMutableLiveData;
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<Location>();

    public MainViewModel(Application application){
        super(application);
        init();
    }

    public void init()
    {
        if (quakeResponseModelMutableLiveData != null && locationMutableLiveData != null)
        {
            return;
        }
        MainLocationListener locationListener = MainLocationListener.getInstance();
        Repository repository = Repository.getInstance();
        Log.e("onMainViewModel", "MainViewModel is running");
        quakeResponseModelMutableLiveData = repository.getQuakeRequest();
        locationListener.startListiningUserLocation(getApplication().getApplicationContext(), this);
    }

    public LiveData<List<Result>> getNewQuakes(){
        Log.e("LiveData", "New Quakes");
        return quakeResponseModelMutableLiveData;
    }

    public LiveData<Location> getLocation(){
        return locationMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    @Override
    public void onLocationChange(Location location) {
        locationMutableLiveData.setValue(location);
        Log.e("MainLocationListener", "listener listen trigger");
    }
}
