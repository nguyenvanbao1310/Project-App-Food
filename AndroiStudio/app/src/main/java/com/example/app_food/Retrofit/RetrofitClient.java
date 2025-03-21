package com.example.app_food.Retrofit;
import com.example.app_food.api.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Đường dẫn API
                    .addConverterFactory(GsonConverterFactory.create()) // Chuyển JSON thành Object
                    .build();
        }
        return retrofit;
    }
    public static UserService getApiUserService() {
        return getRetrofit().create(UserService.class);
    }
}
