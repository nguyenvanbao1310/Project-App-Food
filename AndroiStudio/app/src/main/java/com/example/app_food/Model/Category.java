package com.example.app_food.Model;

public class Category {
    private int id;
    private String name;
    private String images;

    public Category(int id ,String name, String images) {
        this.id = id;
        this.name = name;
        this.images = images;
    }

    public int getId() {return id;}

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return images;
    }
}