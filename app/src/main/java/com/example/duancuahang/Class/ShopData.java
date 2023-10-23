package com.example.duancuahang.Class;

public class ShopData {
    private int status;
    private String idShop, shopPhoneNumber, password, shopOwner, shopName,
            shopAddress, shopEmail, taxCode, cccdFront, cccdBack;

    public String getIdShop() {
        return idShop;
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

    public String getCccdFront() {
        return cccdFront;
    }

    public void setCccdFront(String cccdFront) {
        this.cccdFront = cccdFront;
    }

    public String getCccdBack() {
        return cccdBack;
    }

    public void setCccdBack(String cccdBack) {
        this.cccdBack = cccdBack;
    }

    public ShopData(String idShop, int trangThai, String shopPhoneNumber, String password, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String cccdFront, String cccdBack) {
        this.idShop = idShop;
        this.status = trangThai;
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
        this.cccdFront = cccdFront;
        this.cccdBack = cccdBack;
    }

    public ShopData() {
    }
}
