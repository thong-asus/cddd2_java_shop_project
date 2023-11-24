package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.ActionToGetVoucher;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.Voucher;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class VoucherItem_Adapter extends RecyclerView.Adapter<VoucherItem_ViewHolder>{

    private ArrayList<Voucher> arrVoucher = new ArrayList<>();
    private Context context;

    public VoucherItem_Adapter(ArrayList<Voucher> arrVoucher, Context context) {
        this.arrVoucher = arrVoucher;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherItem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherItem_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_voucher,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherItem_ViewHolder holder, int position) {
        Voucher voucher = arrVoucher.get(position);
        setInformationProduct(voucher.getIdProduct(),voucher.getIdShop(),holder);
        setImageProduct(voucher.getIdProduct(),holder);
        getActionToGetVoucher(voucher.getIdActionToGetVoucher(),holder);
        holder.itemVoucherCode_CustomerItemVoucher.setText(voucher.getIdVoucher());

    }

//    hàm lấy hành động để được voucher
    private void getActionToGetVoucher(String idAction,VoucherItem_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TakeActionToGetVoucher/" +idAction);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ActionToGetVoucher actionToGetVoucher = snapshot.getValue(ActionToGetVoucher.class);
                    holder.tvAction_CustomerItemVoucher.setText(actionToGetVoucher.getValueAction());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    lấy hình ảnh sản phẩm
    private void setImageProduct(String idProduct, VoucherItem_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ImageProducts/"+idProduct+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String url = snapshot.child("urlImage").getValue().toString();
                    Picasso.get().load(url).placeholder(R.drawable.icondowload).into(holder.imgProduct_CustomerItemVoucher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInformationProduct(String idProduct,String idShop, VoucherItem_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+idShop+"/"+idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData= snapshot.getValue(ProductData.class);
                    holder.idProduct_CustomerItemVoucher.setText(productData.getIdProduct());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrVoucher.size();
    }
}
