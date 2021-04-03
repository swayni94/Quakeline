package com.example.common.RestApi;

import com.example.common.RestApi.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IService {
    @GET("/deprem/live.php")
    Call<ResponseModel> getQuakes();
}
