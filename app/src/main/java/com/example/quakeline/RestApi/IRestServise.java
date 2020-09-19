package com.example.quakeline.RestApi;

import com.example.quakeline.RestApi.Model.QuakeResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IRestServise {
    @GET("/deprem/live.php")
    Call<QuakeResponseModel> getQuakes();
}
