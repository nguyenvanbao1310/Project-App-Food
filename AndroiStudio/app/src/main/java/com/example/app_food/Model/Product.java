package com.example.app_food.Model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("rating")
    private double rating;

    @SerializedName("image")
    private String image;

    @SerializedName("categoryId")
    private int categoryId;

    public Product(int id, String name, double price, String description, double rating, String image, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.image = image;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public int getCategoryId() {
        return categoryId;
    }
}