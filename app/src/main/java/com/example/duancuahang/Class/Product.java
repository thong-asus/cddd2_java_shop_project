package com.example.duancuahang.Class;

public class Product {
   private String idProduct;
   private String nameProduct;
   private String imgProduct;
   private int priceProduct;
   private int categoryProduct;
   private int manufaceProduct;
   private int quanlityProduct;
   private int descriptionProduct;
   private int sumLike;
   private double overageCmtProduct;

    public String getIdProduct() {
        return idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(int categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public int getManufaceProduct() {
        return manufaceProduct;
    }

    public void setManufaceProduct(int manufaceProduct) {
        this.manufaceProduct = manufaceProduct;
    }

    public int getQuanlityProduct() {
        return quanlityProduct;
    }

    public void setQuanlityProduct(int quanlityProduct) {
        this.quanlityProduct = quanlityProduct;
    }

    public int getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(int descriptionProduct) {
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

    public Product(String nameProduct, String imgProduct, int priceProduct, int categoryProduct, int manufaceProduct, int quanlityProduct, int descriptionProduct, int sumLike, double overageCmtProduct) {
        this.nameProduct = nameProduct;
        this.imgProduct = imgProduct;
        this.priceProduct = priceProduct;
        this.categoryProduct = categoryProduct;
        this.manufaceProduct = manufaceProduct;
        this.quanlityProduct = quanlityProduct;
        this.descriptionProduct = descriptionProduct;
        this.sumLike = sumLike;
        this.overageCmtProduct = overageCmtProduct;
    }

    public Product() {
    }

    public Product(String idProduct, String nameProduct, String imgProduct, int priceProduct, int categoryProduct, int manufaceProduct, int quanlityProduct, int descriptionProduct, int sumLike, double overageCmtProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.imgProduct = imgProduct;
        this.priceProduct = priceProduct;
        this.categoryProduct = categoryProduct;
        this.manufaceProduct = manufaceProduct;
        this.quanlityProduct = quanlityProduct;
        this.descriptionProduct = descriptionProduct;
        this.sumLike = sumLike;
        this.overageCmtProduct = overageCmtProduct;
    }
}
