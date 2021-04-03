package com.example.quakeline.ViewPage;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.example.common.RestApi.Model.Result;
import com.example.common.RestApi.Repository;
import com.example.common.helper.UserLocationListener;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel implements UserLocationListener.IUserLocationListener {

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
        UserLocationListener locationListener = UserLocationListener.getInstance();
        Repository repository = Repository.getInstance();
        Log.e("onMainViewModel", "MainViewModel is running");
        quakeResponseModelMutableLiveData = repository.getQuakeRequest();
        locationListener.startListeningUserLocation(getApplication().getApplicationContext(), this);
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
