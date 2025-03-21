package com.example.app_food.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_food.Adapter.CategoryAdapter;
import com.example.app_food.api.ApiService;
import com.example.app_food.model.Category;
import com.example.app_food.Retrofit.RetrofitClient;

import com.example.app_food.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Khởi tạo RecyclerView
        recyclerViewCategories = findViewById(R.id.recycler_view_categories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Gọi API
        fetchCategories();
    }

    private void fetchCategories() {
        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    // Log số lượng category
                    Log.d("FetchCategories", "Number of categories: " + categoryList.size());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FetchCategories", "Failed to fetch categories. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Xử lý lỗi và log lỗi
                Log.e("FetchCategories", "Error fetching categories: " + t.getMessage());
            }
        });
    }
}