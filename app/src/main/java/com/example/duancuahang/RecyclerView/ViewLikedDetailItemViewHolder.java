package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewLikedDetailItemViewHolder extends RecyclerView.ViewHolder {
    CircleImageView imgCustomerLiked;
    TextView tvCustomerNameLiked;
    public ViewLikedDetailItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imgCustomerLiked = itemView.findViewById(R.id.imgCustomerLiked);
        tvCustomerNameLiked = itemView.findViewById(R.id.tvCustomerNameLiked);
    }
}
