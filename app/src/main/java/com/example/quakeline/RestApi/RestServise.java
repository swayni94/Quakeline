package com.example.quakeline.RestApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestServise {

    private static String baseURL = "http://api.mert.space";
    private static Retrofit retrofit;


    public static Retrofit getClient(){
        if (retrofit ==null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
