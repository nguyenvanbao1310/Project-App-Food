package com.example.app_food.api;

import com.example.app_food.model.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("auth/register")
    Call<Map<String, String>> signUpPostForm(@Body User user);
    @POST("auth/verify-code")
    Call<Map<String, String>> verifyCode(@Body Map<String, String> requestBody);
    @POST("user/login")
    Call<ResponseBody> login(@Body User request);

}
