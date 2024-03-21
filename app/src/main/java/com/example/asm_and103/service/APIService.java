package com.example.asm_and103.service;

import com.example.asm_and103.Model.Food;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/";

    @GET("/api/list")
    Call<ArrayList<Food>> getFood();

    @Multipart
    @POST("/api/add-list")
    Call<Response<Food>> addFood(@Part MultipartBody.Part image,
                                 @Part("name") RequestBody name,
                                 @Part("price") RequestBody price,
                                 @Part("quantity") RequestBody quantity,
                                 @Part("describe") RequestBody describe);
}
