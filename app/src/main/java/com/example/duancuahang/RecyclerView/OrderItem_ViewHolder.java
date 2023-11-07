package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class OrderItem_ViewHolder extends RecyclerView.ViewHolder {

    ImageView imgItemOrder_Product;
    TextView tvNameCustomer, tvNameProduct, tvPrice, tvAmountProduct, tvTotal, btnDetailOrder;
    public OrderItem_ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgItemOrder_Product = itemView.findViewById(R.id.imgItemOrder_Product);
        tvNameCustomer = itemView.findViewById(R.id.tvNameCustomer);
        tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct);
        tvTotal = itemView.findViewById(R.id.tvTotal);
        btnDetailOrder = itemView.findViewById(R.id.btnDetailOrder);
    }
}
