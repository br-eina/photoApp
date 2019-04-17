package com.example.photoapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    // @GET("get_data")
    // Call<List<Data>> getData();
    @GET("show_data")
    Call<List<Data>> getData();

    @POST("post_data")
    Call<Data> createData(@Body Data data);

    @POST("initiate_processing")
    Call<Data> initiateProcessing(@Body Data data);

}

