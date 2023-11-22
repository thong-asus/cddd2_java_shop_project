package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.FormatMoneyVietNam;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.R;
import com.example.duancuahang.ViewRatingDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Voucher_ChooseItemAdapter extends RecyclerView.Adapter<Voucher_ChooseItemViewHolder> {
    ArrayList<ProductData> productDataArrayList;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private Voucher_ChooseItemAdapter.SelectionProductVoucher selectionProductVoucher;
    public Voucher_ChooseItemAdapter(ArrayList<ProductData> productDataArrayList, Context context, Voucher_ChooseItemAdapter.SelectionProductVoucher selectionProductVoucher){
        this.productDataArrayList = productDataArrayList;
        this.context = context;
        this.selectionProductVoucher = selectionProductVoucher;
    }

    public  interface  SelectionProductVoucher{
        public void getProductSelected(ProductData productData);
        public void getProductNotSelected(ProductData productData);
    }
    @NonNull
    @Override
    public Voucher_ChooseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Voucher_ChooseItemViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_product_voucher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Voucher_ChooseItemViewHolder holder, int position) {

            ProductData productData = productDataArrayList.get(position);
            //Lấy thông tin sản phẩm
            holder.tvNameProductVoucher.setText(productData.getNameProduct());
            getImageProduct(productData.getIdProduct(), holder);
            holder.tvPriceProductVoucher.setText(FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct()) +"đ");

            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.drawable.bg_item01);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_item02);
            }


        holder.chkItemChooseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra đã checked chưa
                boolean isChecked = !holder.chkItemChooseProduct.isChecked();
                //set trạng thái
                holder.chkItemChooseProduct.setChecked(!isChecked);
                if (!isChecked){
                    selectionProductVoucher.getProductSelected(productData);
                }
                else {
                    selectionProductVoucher.getProductNotSelected(productData);
                }
            }
        });
        holder.linearLayout_ProductItemVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra đã checked chưa
                boolean isChecked = holder.chkItemChooseProduct.isChecked();
                //set trạng thái
                holder.chkItemChooseProduct.setChecked(!isChecked);
                if (!isChecked){
                    selectionProductVoucher.getProductSelected(productData);
                }
                else {
                    selectionProductVoucher.getProductNotSelected(productData);
                }
            }
        });
    }
    private void getImageProduct(String idProduct, Voucher_ChooseItemViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("ImageProducts").child(idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Duyệt qua danh sách ảnh của sản phẩm
                    for (DataSnapshot imageItem : snapshot.getChildren()) {
                        // Kiểm tra xem ảnh có URL hay không
                        if (imageItem.child("urlImage").exists()) {
                            String imageUrl = imageItem.child("urlImage").getValue(String.class);
                            // Hiển thị ảnh đầu tiên
                            Picasso.get().load(imageUrl).placeholder(R.drawable.icondowload).into(holder.imgItemProductVoucher);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }


    public void updateData(ArrayList<ProductData> newData) {
        if (newData != null) {
            productDataArrayList = new ArrayList<>(newData);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (productDataArrayList != null) {
            int size = productDataArrayList.size();
            return size;
        }
        return 0;
    }
}
