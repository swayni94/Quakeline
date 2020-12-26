package com.example.quakeline.RestApi;

import android.util.Log;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;
import com.example.quakeline.RestApi.Model.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    private final IRestServise servise;

    private Repository(){
        servise = RestServise.getClient().create(IRestServise.class);
    }

    public MutableLiveData<List<Result>> getQuakeRequest(){
        final MutableLiveData<List<Result>> responseModelMutableLiveData = new MutableLiveData<>();
        servise.getQuakes().enqueue(new Callback<QuakeResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<QuakeResponseModel> call, @NotNull Response<QuakeResponseModel> response) {
                if (response.isSuccessful())
                {
                    responseModelMutableLiveData.postValue(Objects.requireNonNull(response.body()).getResult());
                    Log.e("RepositoryForViewModel", "Repository Is Successful!!");
                }
                else {
                    responseModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuakeResponseModel> call, @NotNull Throwable t) {
                responseModelMutableLiveData.setValue(null);
                Log.e("RepositoryError", "Repository Error!!");
            }
        });
        return responseModelMutableLiveData;
    }


    public ArrayList<Result> getQuakeRequestBackground(){
        ArrayList<Result> results= new ArrayList<>();
        servise.getQuakes().enqueue(new Callback<QuakeResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<QuakeResponseModel> call , @NotNull Response<QuakeResponseModel> response) {
                if (response.isSuccessful()){
                    Log.e("RepositoryForBackground", "Repository Is Successful!!");
                    results.addAll(Objects.requireNonNull(response.body()).getResult());
                }
            }
            @Override
            public void onFailure(@NotNull Call<QuakeResponseModel> call , @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
        return results;
    }

}
