package com.example.food_safari.model;

import java.util.ArrayList;

public class UploadItem {

    private String pname, description, price, category, image;
    private ArrayList<CategoryTwo> data;

    public UploadItem() {
    }

    public UploadItem(String pname, String description, String price, String category, String image, ArrayList<CategoryTwo> data) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.data = data;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<CategoryTwo> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryTwo> data) {
        this.data = data;
    }
}