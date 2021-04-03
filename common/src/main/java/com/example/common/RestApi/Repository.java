package com.example.common.RestApi;

import android.util.Log;

import com.example.common.RestApi.Model.ResponseModel;
import com.example.common.RestApi.Model.Result;

import org.jetbrains.annotations.NotNull;

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

    private final IService servise;

    private Repository(){
        servise = RestServise.getClient().create(IService.class);
    }

    public MutableLiveData<List<Result>> getQuakeRequest(){
        final MutableLiveData<List<Result>> responseModelMutableLiveData = new MutableLiveData<>();
        servise.getQuakes().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseModel> call, @NotNull Response<ResponseModel> response) {
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
            public void onFailure(@NotNull Call<ResponseModel> call, @NotNull Throwable t) {
                responseModelMutableLiveData.setValue(null);
                Log.e("RepositoryError", "Repository Error!!");
            }
        });
        return responseModelMutableLiveData;
    }
}
