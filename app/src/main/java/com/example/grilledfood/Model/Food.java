package com.example.grilledfood.Model;

public class Food {
    String NameOfFood, Image, Description, Price, Discount, MenuId, ExtraOrder, ExtraPrice;
    String HalfOrderName;
    String HalfOrderPrice;
    String HalfOrderQuantity;
    String QuarterOrderName;
    String QuarterOrderPrice;
    String QuarterOrderQuantity;


    public Food(String nameOfFood, String image, String description, String price, String discount, String menuId) {
        NameOfFood = nameOfFood;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
    }

    public Food(String nameOfFood, String image, String description, String price, String discount, String menuId, String extraOrder, String extraPrice) {
        NameOfFood = nameOfFood;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
        ExtraOrder = extraOrder;
        ExtraPrice = extraPrice;
    }

    public Food(String nameOfFood, String image, String description, String price, String discount, String menuId, String extraOrder, String extraPrice, String halfOrderName, String halfOrderPrice, String quarterOrderName, String quarterOrderPrice) {
        NameOfFood = nameOfFood;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
        ExtraOrder = extraOrder;
        ExtraPrice = extraPrice;
        HalfOrderName = halfOrderName;
        HalfOrderPrice = halfOrderPrice;
        QuarterOrderName = quarterOrderName;
        QuarterOrderPrice = quarterOrderPrice;
    }

    public String getHalfOrderName() {
        return HalfOrderName;
    }

    public void setHalfOrderName(String halfOrderName) {
        HalfOrderName = halfOrderName;
    }

    public String getHalfOrderPrice() {
        return HalfOrderPrice;
    }

    public void setHalfOrderPrice(String halfOrderPrice) {
        HalfOrderPrice = halfOrderPrice;
    }

    public String getHalfOrderQuantity() {
        return HalfOrderQuantity;
    }

    public void setHalfOrderQuantity(String halfOrderQuantity) {
        HalfOrderQuantity = halfOrderQuantity;
    }

    public String getQuarterOrderName() {
        return QuarterOrderName;
    }

    public void setQuarterOrderName(String quarterOrderName) {
        QuarterOrderName = quarterOrderName;
    }

    public String getQuarterOrderPrice() {
        return QuarterOrderPrice;
    }

    public void setQuarterOrderPrice(String quarterOrderPrice) {
        QuarterOrderPrice = quarterOrderPrice;
    }

    public String getQuarterOrderQuantity() {
        return QuarterOrderQuantity;
    }

    public void setQuarterOrderQuantity(String quarterOrderQuantity) {
        QuarterOrderQuantity = quarterOrderQuantity;
    }

    public Food() {
    }

    public String getExtraOrder() {
        return ExtraOrder;
    }

    public void setExtraOrder(String extraOrder) {
        ExtraOrder = extraOrder;
    }

    public String getExtraPrice() {
        return ExtraPrice;
    }

    public void setExtraPrice(String extraPrice) {
        ExtraPrice = extraPrice;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
