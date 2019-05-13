package com.example.grilledfood.Model;

public class Category {
    private String NameOfFood;
    private String Image;


    public Category(String name, String image) {
        NameOfFood = name;
        Image = image;
    }

    public Category(){}


    public String getImage() {
        return Image;
    }

    public void setName(String name) {
        NameOfFood = name;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getNameOfFood() {
        return NameOfFood;
    }

    public void setNameOfFood(String nameOfFood) {
        NameOfFood = nameOfFood;
    }
}
