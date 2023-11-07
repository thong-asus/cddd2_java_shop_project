package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class ListImageProductViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImageItemProduct;
    public ListImageProductViewHolder(@NonNull View itemView) {
        super(itemView);
        ivImageItemProduct = itemView.findViewById(R.id.ivProductItem_AddProductMulp);
    }
}
