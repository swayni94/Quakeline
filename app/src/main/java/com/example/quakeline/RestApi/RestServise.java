package com.example.quakeline.RestApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestServise {

    private static final String baseURL = "https://api.orhanaydogdu.com.tr";
    private static Retrofit retrofit;


    public static Retrofit getClient(){
        if (retrofit ==null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
