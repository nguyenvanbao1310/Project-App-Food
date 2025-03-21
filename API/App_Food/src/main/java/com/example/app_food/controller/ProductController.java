package com.example.app_food.Controller;

import com.example.app_food.Entity.Product;
import com.example.app_food.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

//    @GetMapping("/sorted")
//    public List<Product> getSortedProducts() {
//        List<Product> products = productService.getAllProductsSortedByCategoryAndPrice();
//        return products;
//    }
}
