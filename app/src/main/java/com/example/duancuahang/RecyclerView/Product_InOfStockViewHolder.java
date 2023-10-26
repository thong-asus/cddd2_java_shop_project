package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class Product_InOfStockViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProductItem;
    TextView tvIdProductItem,tvNameProductItem, tvCategory, tvManufaceProduct, tvPriceProduct, tvQuanlityProduct;
    Button btnUpdateProductItem, btnDeleteProductItem, btnDetailProductItem;
    public Product_InOfStockViewHolder(@NonNull View itemView) {
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
