package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class ItemProductStatisticsViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProductItem;
    TextView tvIdProductItem,tvNameProductItem, tvCategory, tvManufaceProduct, tvPriceProduct, tvQuanlityProduct;
    public ItemProductStatisticsViewHolder(@NonNull View itemView) {
        super(itemView);
        imgProductItem = itemView.findViewById(R.id.ivProduct_ScreenCustomerItemProductList_statics);
        tvIdProductItem = itemView.findViewById(R.id.tvIdProductItem_statics);
        tvNameProductItem = itemView.findViewById(R.id.tvNameProductItem_statics);
        tvCategory = itemView.findViewById(R.id.tvCategoryProductItem_statics);
        tvManufaceProduct = itemView.findViewById(R.id.tvManufaceProductItem_statics);
        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct_OrderDetail_statics);
        tvQuanlityProduct = itemView.findViewById(R.id.tvQuanlityProduct_statics);
    }
}
