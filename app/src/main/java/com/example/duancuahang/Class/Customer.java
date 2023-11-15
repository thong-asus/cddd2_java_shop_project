package com.example.duancuahang.Class;

import java.io.Serializable;

public class Customer implements Serializable {
    private String id;
    private String address;
    private String imageUser;
    private String name;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", imageUser='" + imageUser + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer() {
    }

    public Customer(String id, String address, String imageUser, String name) {
        this.id = id;
        this.address = address;
        this.imageUser = imageUser;
        this.name = name;
    }
}
