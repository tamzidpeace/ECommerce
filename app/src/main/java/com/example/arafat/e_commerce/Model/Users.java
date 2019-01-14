package com.example.arafat.e_commerce.Model;

public class Users {

    private String name;
    private String password;
    private String phone;
    private String address;
    private String image;


    public Users() {
    }

    public Users(String name, String password, String phone, String address, String image) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}
