package com.example.app_food.api;

import com.example.app_food.model.Category;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("categories")
    Call<List<Category>> getCategories();
}
