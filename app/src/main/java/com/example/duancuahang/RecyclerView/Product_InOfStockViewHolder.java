package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class Product_InOfStockViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProductItem;
    TextView tvIdProductItem,tvNameProductItem, tvCategory, tvManufaceProduct, tvPriceProduct, tvQuanlityProduct;
    Button  btnDeleteProductItem, btnDetailProductItem;
    public Product_InOfStockViewHolder(@NonNull View itemView) {
        super(itemView);
        tvIdProductItem = itemView.findViewById(R.id.tvIdProductItem);
        tvNameProductItem = itemView.findViewById(R.id.tvNameProductItem);
        tvCategory = itemView.findViewById(R.id.tvCategoryProductItem);
        tvManufaceProduct = itemView.findViewById(R.id.tvManufaceProductItem);
        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct_OrderDetail);
        tvQuanlityProduct = itemView.findViewById(R.id.tvQuanlityProduct);
        btnDeleteProductItem = itemView.findViewById(R.id.btnDeleteProductItem);
        btnDetailProductItem = itemView.findViewById(R.id.btnDetailProducItem);
        imgProductItem = itemView.findViewById(R.id.ivProduct_ScreenCustomerItemProductList);
    }
}
