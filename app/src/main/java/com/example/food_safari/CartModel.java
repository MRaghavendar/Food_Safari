package com.example.food_safari;

public class CartModel {
    private String age,id, price, quantity;

    public CartModel() {
    }

    public CartModel(String age, String id, String price, String quantity) {
        this.age = age;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
