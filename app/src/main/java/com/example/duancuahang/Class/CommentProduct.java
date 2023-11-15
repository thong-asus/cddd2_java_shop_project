package com.example.duancuahang.Class;

public class CommentProduct {
    private String contentComment, dateComment, idCustomer, idProduct;

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public CommentProduct(String contentComment, String dateComment, String idCustomer, String idProduct) {
        this.contentComment = contentComment;
        this.dateComment = dateComment;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
    }

    public CommentProduct() {
    }
}
