package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class Voucher_ChooseItemViewHolder extends RecyclerView.ViewHolder {
    CheckBox chkItemChooseProduct;
    ImageView imgItemProductVoucher;
    TextView tvNameProductVoucher, tvPriceProductVoucher;
    LinearLayout linearLayout_ProductItemVoucher;
    public Voucher_ChooseItemViewHolder(@NonNull View itemView) {
        super(itemView);
        chkItemChooseProduct = itemView.findViewById(R.id.chkItemChooseProduct);
        imgItemProductVoucher = itemView.findViewById(R.id.imgItemProductVoucher);
        tvNameProductVoucher = itemView.findViewById(R.id.tvNameProductVoucher);
        tvPriceProductVoucher = itemView.findViewById(R.id.tvPriceProductVoucher);
        linearLayout_ProductItemVoucher = itemView.findViewById(R.id.linearLayout_ProductItemVoucher);
    }
}
