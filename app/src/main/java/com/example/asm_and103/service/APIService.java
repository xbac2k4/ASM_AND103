package com.example.asm_and103.service;

import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.Model.Fruit;
import com.example.asm_and103.Model.Response;
import com.example.asm_and103.Model.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String ipv4 = "10.0.2.2";
    String DOMAIN = "http://"+ ipv4 +":3000/api/";
    @POST("add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @PUT("update-distributor-by-id/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id,@Body Distributor distributor);
    @DELETE("delete-distributor-by-id/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);
    @GET("search-distributor-by-name")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("name") String name);

    @GET("get-distributor")
    Call<Response<ArrayList<Distributor>>> getDistributor();
    @GET("get-fruit")
    Call<Response<ArrayList<Fruit>>> getFruits();
    @DELETE("delete-fruit-by-id/{id}")
    Call<Response<Void>> deleteFruit(@Path("id") String id);
    //lab 6
    @Multipart
    @POST("add-fruit-with-file-image")
    Call<Response<Fruit>> addFruitWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part ArrayList<MultipartBody.Part> ds_hinh);
    @Multipart
    @PUT("update-fruit-by-id/{id}")
    Call<Response<Void>> updateFruitWithFileImage(@Path("id") String id,
                                                  @PartMap Map<String, RequestBody> requestBodyMap,
                                                  @Part ArrayList<MultipartBody.Part> ds_hinh);
    @GET("get-fruit-authenticate-token")
    Call<Response<ArrayList<Fruit>>> getFruitToken(@Header("Authorization") String token);

    @GET("search-fruit-by-name")
    Call<Response<ArrayList<Fruit>>> searchFruitByName(@Query("name") String name);
    @Multipart
    @POST("register-send-email")
    Call<Response<User>> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avartar);
    @POST("login")
    Call<Response<User>> login (@Body User user);
    @GET("get-list-fruit")
    Call<Response<ArrayList<Fruit>>> getListFruit(@Header("Authorization")String token);
}
