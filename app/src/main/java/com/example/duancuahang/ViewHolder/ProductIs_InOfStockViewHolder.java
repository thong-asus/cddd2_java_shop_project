package com.example.duancuahang.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Product;
import com.example.duancuahang.R;

import java.util.List;

public class ProductIs_InOfStockViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProductItem;
    TextView tvIdProductItem,tvNameProductItem, tvCategory, tvManufaceProduct, tvPriceProduct, tvQuanlityProduct;
    Button btnUpdateProductItem, btnDeleteProductItem, btnDetailProductItem;

    public ImageView getImgProductItem() {
        return imgProductItem;
    }

    public TextView getTvIdProductItem() {
        return tvIdProductItem;
    }

    public TextView getTvNameProductItem() {
        return tvNameProductItem;
    }

    public TextView getTvCategory() {
        return tvCategory;
    }

    public TextView getTvManufaceProduct() {
        return tvManufaceProduct;
    }

    public TextView getTvPriceProduct() {
        return tvPriceProduct;
    }

    public TextView getTvQuanlityProduct() {
        return tvQuanlityProduct;
    }

    public Button getBtnUpdateProductItem() {
        return btnUpdateProductItem;
    }

    public Button getBtnDeleteProductItem() {
        return btnDeleteProductItem;
    }

    public Button getBtnDetailProductItem() {
        return btnDetailProductItem;
    }

    public ProductIs_InOfStockViewHolder(@NonNull View itemView) {
        super(itemView);
        tvIdProductItem = itemView.findViewById(R.id.tvIdProductItem);
        tvNameProductItem = itemView.findViewById(R.id.tvNameProductItem);
        tvCategory = itemView.findViewById(R.id.tvCategoryProductItem);
        tvManufaceProduct = itemView.findViewById(R.id.tvManufaceProductItem);
        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
        tvQuanlityProduct = itemView.findViewById(R.id.tvQuanlityProduct);
        btnUpdateProductItem = itemView.findViewById(R.id.btnUpdateProductItem);
        btnDeleteProductItem = itemView.findViewById(R.id.btnDeleteProductItem);
        btnDetailProductItem = itemView.findViewById(R.id.btnDetailProducItem);
        imgProductItem = itemView.findViewById(R.id.ivProduct_ScreenCustomerItemProductList);
    }
}
