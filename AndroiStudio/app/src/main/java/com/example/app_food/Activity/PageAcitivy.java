package com.example.app_food.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_food.Adapter.CategoryAdapter;
import com.example.app_food.Adapter.ProductAdapter;
import com.example.app_food.Model.Category;
import com.example.app_food.Model.Product;
import com.example.app_food.R;
import com.example.app_food.Retrofit.RetrofitClient;
import com.example.app_food.Service.APIService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageAcitivy extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    private APIService apiService;

    private List<Product> originalProductList = new ArrayList<>();
    private List<Product> productList;

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;


    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        recyclerViewCategories = findViewById(R.id.recycler_view_categories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryClickListener(){
            @Override
            public void onCategoryClick(int categoryId)
            {
                filterProductsByCategory(categoryId);
            }
        });
        recyclerViewCategories.setAdapter(categoryAdapter);

        fetchCategories();
        AnhXa();
        GetProducts();

    }

    private void fetchCategories() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    Log.d("FetchCategories", "Number of categories: " + categoryList.size());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FetchCategories", "Failed to fetch categories. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("FetchCategories", "Error fetching categories: " + t.getMessage());
            }
        });
    }
    private void AnhXa()
    {
        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            Log.d("logg", "RecyclerView đã được ánh xạ thành công.");
        } else {
            Log.e("logg", "Lỗi: RecyclerView không tìm thấy trong layout!");
        }

    }

    private void GetProducts(){

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getProductAll().enqueue(new Callback<List<Product>>(){
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response){
                if (response.isSuccessful()){

                    Log.d("logg", "API gọi thành công!");
                    productList = response.body();
                    originalProductList.clear(); // Cập nhật danh sách gốc
                    originalProductList.addAll(productList);
                    for (Product product : productList) {
                        Log.d("logg", "Product: " + product.getName() + ", Image: " + product.getImage());
                    }
                    if (productList != null && !productList.isEmpty()) {
                        Log.d("logg", "Số lượng category nhận được: " + productList.size());
                    } else {
                        Log.e("logg", "Danh sách category rỗng hoặc null!");
                    }
                    adapter = new ProductAdapter(productList);
                    recyclerView.setLayoutManager(new GridLayoutManager(PageAcitivy.this, 2));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else {
                    int statusCode = response.code();
                    Log.e("logg", "Lỗi khi gọi API: " );

                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Log.d("logg", t.getMessage());
                Log.e("logg", "Lỗi khi gọi API: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }



    private void filterProductsByCategory(int categoryId) {
        selectedCategoryId = categoryId;

        if (categoryId == -1) {
            productList.clear();
            productList.addAll(originalProductList);
        }
        else {
            // Lọc sản phẩm theo danh mục
            List<Product> filteredProducts = new ArrayList<>();
            for (Product product : originalProductList) {  // Dùng originalProductList
                if (product.getCategoryId() == categoryId) {
                    filteredProducts.add(product);
                }
            }
            productList.clear();
            productList.addAll(filteredProducts);
        }
        // **Sắp xếp danh sách sản phẩm theo giá tăng dần**
        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Double.compare(p1.getPrice(), p2.getPrice()); // So sánh theo giá tiền
            }
        });
        adapter.notifyDataSetChanged();

        Log.d("logg", "Số sản phẩm trong category " + categoryId + ": " + productList.size());
    }

}
