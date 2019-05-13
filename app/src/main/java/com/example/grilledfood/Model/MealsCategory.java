package com.example.grilledfood.Model;

public class MealsCategory {
    private String NameOfFood;
    private String Image;

    public MealsCategory(String nameOfFood, String image) {
        NameOfFood = nameOfFood;
        Image = image;
    }

    public MealsCategory() {
    }

    public String getNameOfFood() {
        return NameOfFood;
    }

    public void setNameOfFood(String nameOfFood) {
        NameOfFood = nameOfFood;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
