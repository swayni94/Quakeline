package com.example.quakeline.RestApi;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {


    private static Repository repository;

    public static Repository getInstance() {
        if (repository == null)
        {
            repository = new Repository();
        }
        return repository;
    }

    private IRestServise servise;

    public Repository(){
        servise = RestServise.getClient().create(IRestServise.class);
    }

    public MutableLiveData<QuakeResponseModel> getQuakeRequest(){
        final MutableLiveData<QuakeResponseModel> responseModelMutableLiveData = new MutableLiveData<>();

        servise.getQuakes().enqueue(new Callback<QuakeResponseModel>() {
            @Override
            public void onResponse(Call<QuakeResponseModel> call, Response<QuakeResponseModel> response) {
                if (response.isSuccessful())
                {
                    responseModelMutableLiveData.postValue(response.body());
                }
                else {
                    responseModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<QuakeResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(null);
            }
        });
        return responseModelMutableLiveData;
    }


}
