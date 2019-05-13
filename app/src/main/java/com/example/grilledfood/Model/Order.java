package com.example.grilledfood.Model;

public class Order {
    private String UserPhone;
    private String ProductId;
    private String ProductName;
    private String Price;
    private String Discount;
    private String Quantity;
    private String Image;

    private String ExtraOrder;
    private String ExtraPrice;
    private String ExtraQuantity;

    private String HalfOrderName;
    private String HalfOrderPrice;
    private String HalfOrderQuantity;

    private String QuarterOrderName;
    private String QuarterOrderPrice;
    private String QuarterOrderQuantity;


    public Order(String productId, String productName, String quantity, String price, String discount, String image) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Discount = discount;
        Quantity = quantity;
        Image = image;
    }

    public Order(String userPhone, String productId, String productName, String quantity, String price, String discount, String image) {
        UserPhone = userPhone;
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Discount = discount;
        Quantity = quantity;
        Image = image;
    }

    public Order(String userPhone, String productId, String productName, String quantity, String price, String discount, String image, String extraOrder, String extraPrice, String extraQuantity) {
        UserPhone = userPhone;
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Discount = discount;
        Quantity = quantity;
        Image = image;
        ExtraOrder = extraOrder;
        ExtraPrice = extraPrice;
        ExtraQuantity = extraQuantity;
    }

    public Order(String userPhone, String productId, String productName, String quantity, String price, String discount, String image, String extraOrder, String extraPrice, String extraQuantity, String halfOrderName, String halfOrderPrice, String halfOrderQuantity, String quarterOrderName, String quarterOrderPrice, String quarterOrderQuantity) {
        UserPhone = userPhone;
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Discount = discount;
        Quantity = quantity;
        Image = image;
        ExtraOrder = extraOrder;
        ExtraPrice = extraPrice;
        ExtraQuantity = extraQuantity;
        HalfOrderName = halfOrderName;
        HalfOrderPrice = halfOrderPrice;
        HalfOrderQuantity = halfOrderQuantity;
        QuarterOrderName = quarterOrderName;
        QuarterOrderPrice = quarterOrderPrice;
        QuarterOrderQuantity = quarterOrderQuantity;
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

    public String getExtraQuantity() {
        return ExtraQuantity;
    }

    public void setExtraQuantity(String extraQuantity) {
        ExtraQuantity = extraQuantity;
    }

    public Order() {
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
