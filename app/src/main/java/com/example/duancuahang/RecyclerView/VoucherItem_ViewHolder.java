package com.example.duancuahang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class VoucherItem_ViewHolder extends RecyclerView.ViewHolder {
ImageView imgProduct_CustomerItemVoucher;
TextView itemVoucherCode_CustomerItemVoucher,idProduct_CustomerItemVoucher,tvAction_CustomerItemVoucher;


    public VoucherItem_ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgProduct_CustomerItemVoucher = itemView.findViewById(R.id.imgProduct_CustomerItemVoucher);
        itemVoucherCode_CustomerItemVoucher = itemView.findViewById(R.id.itemVoucherCode_CustomerItemVoucher);
        idProduct_CustomerItemVoucher = itemView.findViewById(R.id.idProduct_CustomerItemVoucher);
        tvAction_CustomerItemVoucher = itemView.findViewById(R.id.tvAction_CustomerItemVoucher);

    }
}
