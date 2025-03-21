package com.example.app_food.Model;

public class Category {
    private String id;
    private String name;
    private String images;

    public Category(String id ,String name, String images) {
        this.id = id;
        this.name = name;
        this.images = images;
    }

    public String getId() {return id;}

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return images;
    }
}