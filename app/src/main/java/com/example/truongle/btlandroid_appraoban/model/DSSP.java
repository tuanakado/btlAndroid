package com.example.truongle.btlandroid_appraoban.model;

/**
 * Created by truongle on 11/05/2017.
 */

public class DSSP {
    private String name, image, address, product_key, category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DSSP() {

    }

    public DSSP(String name, String image, String address) {

        this.name = name;
        this.image = image;
        this.address = address;
    }
}
