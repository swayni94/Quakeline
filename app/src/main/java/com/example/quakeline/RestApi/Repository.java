package com.example.quakeline.RestApi;

import android.util.Log;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;
import com.example.quakeline.RestApi.Model.Result;

import java.util.List;
import java.util.Objects;

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

    private Repository(){
        servise = RestServise.getClient().create(IRestServise.class);
    }

    public MutableLiveData<List<Result>> getQuakeRequest(){
        final MutableLiveData<List<Result>> responseModelMutableLiveData = new MutableLiveData<>();
        servise.getQuakes().enqueue(new Callback<QuakeResponseModel>() {
            @Override
            public void onResponse(Call<QuakeResponseModel> call, Response<QuakeResponseModel> response) {
                if (response.isSuccessful())
                {
                    responseModelMutableLiveData.postValue(response.body().getResult());
                    Log.e("RepositoryIsSuccessfull", "Repository Is Successfull!!");
                }
                else {
                    responseModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<QuakeResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(null);
                Log.e("RepositoryError", "Repository Error!!");
            }
        });
        return responseModelMutableLiveData;
    }


}
