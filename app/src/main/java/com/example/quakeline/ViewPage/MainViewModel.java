package com.example.quakeline.ViewPage;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;
import com.example.quakeline.RestApi.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<QuakeResponseModel> quakeResponseModelMutableLiveData;

    private Repository repository;

    public void init()
    {
        if (quakeResponseModelMutableLiveData != null)
        {
            return;
        }
            repository = Repository.getInstance();
            quakeResponseModelMutableLiveData = repository.getQuakeRequest();

    }

    public LiveData<QuakeResponseModel> getNewQuakes(){
        return quakeResponseModelMutableLiveData;
    }
}
