package com.example.app_food.Model;

public class Category {
    private String name;
    private String images;

    public Category(String name, String images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return images;
    }
}