package com.example.duancuahang.Class;

public class User {
    private String idUser;
    private String nameUser;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public User(String idUser, String nameUser) {
        this.idUser = idUser;
        this.nameUser = nameUser;
    }
}
