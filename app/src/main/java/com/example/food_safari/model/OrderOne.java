package com.example.food_safari.model;

public class OrderOne {

    private String orderNumber, totalPrice, typeOfDelivery, time, address;

    public OrderOne() {
    }

    public OrderOne(String orderNumber, String totalPrice, String typeOfDelivery, String time, String address) {
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.typeOfDelivery = typeOfDelivery;
        this.time = time;
        this.address = address;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
