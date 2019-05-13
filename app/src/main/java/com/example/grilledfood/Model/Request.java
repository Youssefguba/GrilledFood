package com.example.grilledfood.Model;

import java.util.List;

public class Request {
    private String name;
    private String phone;
    private String total;
    private String address;
    private List<Order> foods; // List of food order ...
    private String status;
    private String comment;
    private String GlobalAddress;


    public Request() {
    }

    public Request(String name, String phone, String total, String address, List<Order> foods, String status, String comment) {
        this.name = name;
        this.phone = phone;
        this.total = total;
        this.address = address;
        this.foods = foods;
        this.status = status;
        this.comment = comment;
    }

    public Request(String name, String phone, String total, String address, List<Order> foods, String status, String comment, String globalAddress) {
        this.name = name;
        this.phone = phone;
        this.total = total;
        this.address = address;
        this.foods = foods;
        this.status = status;
        this.comment = comment;
        this.GlobalAddress = globalAddress;
    }


    public String getGlobalAddress() {
        return GlobalAddress;
    }

    public void setGlobalAddress(String globalAddress) {
        GlobalAddress = globalAddress;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
