package com.example.food_safari.model;

//Storing the user details to the Firebase database
public class User {
    private String name,email,password,address,phonenumber;


    public User() {
    }

    public User(String name, String email, String password, String address, String phonenumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phonenumber = phonenumber;

    }

    //getter methods helps to get the data from the firebase
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

}

