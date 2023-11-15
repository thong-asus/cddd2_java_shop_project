package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewCommentedDetailItemViewHolder extends RecyclerView.ViewHolder {
    CircleImageView imgCustomerCommented;
    TextView tvCustomerNameCommented, tvDateCommented, tvContentCommented;
    public ViewCommentedDetailItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imgCustomerCommented = itemView.findViewById(R.id.imgCustomerCommented);
        tvCustomerNameCommented = itemView.findViewById(R.id.tvCustomerNameCommented);
        tvDateCommented = itemView.findViewById(R.id.tvDateCommented);
        tvContentCommented = itemView.findViewById(R.id.tvContentCommented);
    }
}
