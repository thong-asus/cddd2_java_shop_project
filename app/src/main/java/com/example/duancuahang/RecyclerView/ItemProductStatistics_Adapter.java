package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.FormatMoneyVietNam;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Dialog.ProductStatisticsDialog;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemProductStatistics_Adapter extends RecyclerView.Adapter<ItemProductStatisticsViewHolder> {
    ArrayList<ProductData> products;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public ItemProductStatistics_Adapter(ArrayList<ProductData> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemProductStatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemProductStatisticsViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_productstatistics, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemProductStatisticsViewHolder holder, int position) {
        ProductData product = products.get(position);
        setInformationProduct_Item(holder, product);
        setCategoryProduct_KetCategory(holder, product);
        setManuface_keyManufaceProduct(holder, product);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.bg_item01);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_item02);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductStatisticsDialog productStatisticsDialog = new ProductStatisticsDialog(product);
                productStatisticsDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Thống kê sản phẩm ");
            }
        });
    }

    private void setCategoryProduct_KetCategory(ItemProductStatisticsViewHolder holder, ProductData productData) {

        databaseReference = firebaseDatabase.getReference("Category");
        Query query = databaseReference.orderByChild("idCategory").equalTo(productData.getKeyCategoryProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot categoryItem : snapshot.getChildren()) {
                        String idCategory = categoryItem.child("idCategory").getValue().toString();
                        String nameCategory = categoryItem.child("nameCategory").getValue().toString();
                        Category category = new Category(idCategory, idCategory, nameCategory);
                        holder.tvCategory.setText(category.getNameCategory());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm lấy manuface theo key manuface của product
    private void setManuface_keyManufaceProduct(ItemProductStatisticsViewHolder holder, ProductData productData) {
        databaseReference = firebaseDatabase.getReference("Manuface");
        Query query = databaseReference.orderByChild("idManuface").equalTo(productData.getKeyManufaceProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot manufaceItem : snapshot.getChildren()) {
                    String idManuface = manufaceItem.child("idManuface").getValue().toString();
                    String keyManuface_Category = manufaceItem.child("keyManuface_Category").getValue().toString();
                    String nameManuface = manufaceItem.child("nameManuface").getValue().toString();
                    Manuface manuface = new Manuface(idManuface, idManuface, nameManuface, keyManuface_Category);
                    holder.tvManufaceProduct.setText(manuface.getNameManuface());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm tải lại giao diện product
    private void setInformationProduct_Item(ItemProductStatisticsViewHolder holder, ProductData productData) {
        holder.tvIdProductItem.setText(productData.getIdProduct());
        holder.tvNameProductItem.setText(productData.getNameProduct());
        holder.tvPriceProduct.setText("Giá: " + FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct()) + "đ");
        holder.tvQuanlityProduct.setText("Số lượng: " + productData.getQuanlityProduct());
        databaseReference = firebaseDatabase.getReference("ImageProducts");

        databaseReference = firebaseDatabase.getReference("ImageProducts").child(productData.getIdProduct());
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
                            Picasso.get().load(imageUrl).placeholder(R.drawable.icondowload).into(holder.imgProductItem);
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


    @Override
    public int getItemCount() {
        return products.size();
    }
}
