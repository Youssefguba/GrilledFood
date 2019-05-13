package com.example.grilledfood.Model;

public class User {
    private String Name;
    private String Password;
    private String phone;
    private String IsStaff;
    private String HomeAddress;
    private String GlobalAddress;

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;
        this.IsStaff = "false";
    }
    public User(){}

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public void setName(String Name) { this.Name = Name; }

    public String getName() { return Name; }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }



    public void setPassword(String Password) { this.Password = Password; }

    public String getPassword() { return Password; }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGlobalAddress() {
        return GlobalAddress;
    }

    public void setGlobalAddress(String globalAddress) {
        GlobalAddress = globalAddress;
    }
}
