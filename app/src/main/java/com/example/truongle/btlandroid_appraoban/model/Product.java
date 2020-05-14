package com.example.truongle.btlandroid_appraoban.model;

/**
 * Created by truongle on 29/03/2017.
 */

public class Product {
    private String image;
    private String name;
    private String cost, city;

    private String username;
    private String  user_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Product(String image, String name, String cost, String city, String username, String user_id) {

        this.image = image;
        this.name = name;
        this.cost = cost;
        this.city = city;
        this.username = username;
        this.user_id = user_id;
    }

    public Product() {

    }
}
