package com.example.quakeline.ViewPage;

import android.util.Log;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;
import com.example.quakeline.RestApi.Model.Result;
import com.example.quakeline.RestApi.Repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Result>> quakeResponseModelMutableLiveData;

    private Repository repository;

    public MainViewModel(){
        init();
    }


    public void init()
    {
        if (quakeResponseModelMutableLiveData != null)
        {
            return;
        }
            repository = Repository.getInstance();
            quakeResponseModelMutableLiveData = repository.getQuakeRequest();

    }

    public LiveData<List<Result>> getNewQuakes(){
        Log.e("LiveData", "deneme");
        return quakeResponseModelMutableLiveData;
    }
}
