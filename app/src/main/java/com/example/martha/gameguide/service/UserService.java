package com.example.martha.gameguide.service;


import com.example.martha.gameguide.model.UserModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Martha on 4/12/2016.
 */
public interface UserService {

    @POST("users/sign_up")
    Call<ResponseBundle<UserModel>> sign_up(@Body HashMap<String, Object> registerRequest);

    @POST("users/login")
    Call<ResponseBundle<UserModel>> login(@Body HashMap<String, Object> loginRequest);

    @GET("users/user")
    Call<ResponseBundle<UserModel>> getUser(@Header("TOKEN") String token);

    @PUT("users/update")
    @Multipart
    Call<ResponseBundle<String>> updateUser(@Header("TOKEN") String token, @Part MultipartBody.Part userModel, @Part MultipartBody.Part profPic);

    @POST("users/logout")
    Call<ResponseBundle<String>> logout(@Header("TOKEN") String token);

    @POST("users/change-password")
    Call<ResponseBundle<UserModel>> change_password(@Body HashMap<String, Object> changePasswordRequest);

    @POST("users/forgot-password")
    Call<ResponseBundle<UserModel>> recover_password(@Body HashMap<String, Object> recoverPasswordRequest);


    @GET("users/profile_picture")
    Call<ResponseBody> downloadProfilePicture(@Header("TOKEN") String token);










}