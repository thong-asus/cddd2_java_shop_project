package com.example.duancuahang.Class;

public class ProductData {
    private String keyProductItem;
    private String idProduct;
    private String nameProduct;
    private String urlImageProduct;
    private int priceProduct;
    private String keyCategoryProduct;
    private String keyManufaceProduct;
    private int quanlityProduct;
    private String descriptionProduct;
    private int sumLike;
    private double overageCmtProduct;

    public ProductData() {
    }

    public String getKeyProductItem() {
        return keyProductItem;
    }

    public void setKeyProductItem(String keyProductItem) {
        this.keyProductItem = keyProductItem;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getUrlImageProduct() {
        return urlImageProduct;
    }

    public void setUrlImageProduct(String urlImageProduct) {
        this.urlImageProduct = urlImageProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getKeyCategoryProduct() {
        return keyCategoryProduct;
    }

    public void setKeyCategoryProduct(String keyCategoryProduct) {
        this.keyCategoryProduct = keyCategoryProduct;
    }

    public String getKeyManufaceProduct() {
        return keyManufaceProduct;
    }

    public void setKeyManufaceProduct(String keyManufaceProduct) {
        this.keyManufaceProduct = keyManufaceProduct;
    }

    public int getQuanlityProduct() {
        return quanlityProduct;
    }

    public void setQuanlityProduct(int quanlityProduct) {
        this.quanlityProduct = quanlityProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public int getSumLike() {
        return sumLike;
    }

    public void setSumLike(int sumLike) {
        this.sumLike = sumLike;
    }

    public double getOverageCmtProduct() {
        return overageCmtProduct;
    }

    public void setOverageCmtProduct(double overageCmtProduct) {
        this.overageCmtProduct = overageCmtProduct;
    }

    public ProductData(String idProduct, String nameProduct, String urlImageProduct, int priceProduct, String keyCategoryProduct, String keyManufaceProduct, int quanlityProduct, String descriptionProduct, int sumLike, double overageCmtProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.urlImageProduct = urlImageProduct;
        this.priceProduct = priceProduct;
        this.keyCategoryProduct = keyCategoryProduct;
        this.keyManufaceProduct = keyManufaceProduct;
        this.quanlityProduct = quanlityProduct;
        this.descriptionProduct = descriptionProduct;
        this.sumLike = sumLike;
        this.overageCmtProduct = overageCmtProduct;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "keyProductItem='" + keyProductItem + '\'' +
                ", idProduct='" + idProduct + '\'' +
                ", nameProduct='" + nameProduct + '\'' +
                ", urlImageProduct='" + urlImageProduct + '\'' +
                ", priceProduct=" + priceProduct +
                ", keyCategoryProduct='" + keyCategoryProduct + '\'' +
                ", keyManufaceProduct='" + keyManufaceProduct + '\'' +
                ", quanlityProduct=" + quanlityProduct +
                ", descriptionProduct='" + descriptionProduct + '\'' +
                ", sumLike=" + sumLike +
                ", overageCmtProduct=" + overageCmtProduct +
                '}';
    }
}
