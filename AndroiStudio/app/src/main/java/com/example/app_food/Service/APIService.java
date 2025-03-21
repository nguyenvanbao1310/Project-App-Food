package com.example.app_food.Service;

import com.example.app_food.Model.Category;
import com.example.app_food.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("products")
    Call<List<Product>> getProductAll();

    @GET("categories")
    Call<List<Category>> getCategories();
}

