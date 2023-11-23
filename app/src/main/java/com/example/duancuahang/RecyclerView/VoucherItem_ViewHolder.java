package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class VoucherItem_ViewHolder extends RecyclerView.ViewHolder {
    ImageView imgVoucherProduct;
    LinearLayout linearLayout_VoucherItem;
    TextView itemVoucherCode, itemPercentVoucher;

    public VoucherItem_ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgVoucherProduct = itemView.findViewById(R.id.imgVoucherProduct);
        linearLayout_VoucherItem = itemView.findViewById(R.id.linearLayout_VoucherItem);
        itemVoucherCode = itemView.findViewById(R.id.itemVoucherCode);
        itemPercentVoucher = itemView.findViewById(R.id.itemPercentVoucher);
    }
}
