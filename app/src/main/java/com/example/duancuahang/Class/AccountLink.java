package com.example.duancuahang.Class;

import java.io.Serializable;

public class AccountLink implements Serializable {
    private int idApp;
    private String urlUser ;

    public AccountLink(int idApp, String urlUser) {
        this.idApp = idApp;
        this.urlUser = urlUser;
    }

    @Override
    public String toString() {
        return "AccountLink{}";
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public String getUrlUser() {
        return urlUser;
    }

    public void setUrlUser(String urlUser) {
        this.urlUser = urlUser;
    }
}
