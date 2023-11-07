package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class UriImageFirebase_ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImageItemProduct;
    public UriImageFirebase_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivImageItemProduct = itemView.findViewById(R.id.ivProductItem_AddProductMulp);
    }
}
