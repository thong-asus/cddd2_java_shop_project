package com.example.duancuahang.Class;

import android.net.Uri;

import java.io.Serializable;

public class ImageProduct implements Serializable {

    private String idImag_product;
    private Uri urlImage;

    @Override
    public String toString() {
        return "ImgaProduct{" +
                ", idImag_product='" + idImag_product + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    public String getIdImag_product() {
        return idImag_product;
    }

    public void setIdImag_product(String idImag_product) {
        this.idImag_product = idImag_product;
    }

    public Uri getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(Uri urlImage) {
        this.urlImage = urlImage;
    }

    public ImageProduct(String idImag_product, Uri urlImage) {
        this.idImag_product = idImag_product;
        this.urlImage = urlImage;
    }

    public ImageProduct() {
        this.idImag_product = null;
        this.urlImage = null;
    }
}
