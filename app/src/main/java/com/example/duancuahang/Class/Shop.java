package com.example.duancuahang.Class;

import java.io.Serializable;

public class Shop implements Serializable {
    private int status;
    private String idShop, shopPhoneNumber, password, shopOwner, shopName,
            shopAddress, shopEmail, taxCode, imgCCCDFront, imgCCCDBack, imgShopAvatar;

    public String getIdShop() {
        return idShop;
    }

    public String getImgShopAvatar() {
        return imgShopAvatar;
    }

    public Shop(String idShop, String imgShopAvatar) {
        this.idShop = idShop;
        this.imgShopAvatar = imgShopAvatar;
    }

    public void setImgShopAvatar(String imgShopAvatar) {
        this.imgShopAvatar = imgShopAvatar;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getImgCCCDFront() {
        return imgCCCDFront;
    }

    public void setImgCCCDFront(String imgCCCDFront) {
        this.imgCCCDFront = imgCCCDFront;
    }

    public String getImgCCCDBack() {
        return imgCCCDBack;
    }

    public void setImgCCCDBack(String imgCCCDBack) {
        this.imgCCCDBack = imgCCCDBack;
    }

    public Shop(String idShop, int status, String shopPhoneNumber, String password, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String imgCCCDFront, String imgCCCDBack) {
        this.idShop = idShop;
        this.status = status;
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
        this.imgCCCDFront = imgCCCDFront;
        this.imgCCCDBack = imgCCCDBack;
    }

    public Shop(String shopPhoneNumber, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String imgCCCDFront, String imgCCCDBack) {
        this.shopPhoneNumber = shopPhoneNumber;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
        this.imgCCCDFront = imgCCCDFront;
        this.imgCCCDBack = imgCCCDBack;
    }

    public Shop() {
        this.idShop = shopPhoneNumber;
        this.status = 0;
        this.password = null;
    }
}
