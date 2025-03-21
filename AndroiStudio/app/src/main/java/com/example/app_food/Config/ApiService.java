package com.example.app_food.Config;

import com.example.app_food.Model.Category;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("categories")
    Call<List<Category>> getCategories();
}
