package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Detailproduct;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Product_OutOfStockAdater extends RecyclerView.Adapter<Product_OutOfStockViewHolder> {
    ArrayList<ProductData> arrProduct;
    Context context;

    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public Product_OutOfStockAdater(ArrayList<ProductData> arrProduct, Context context){
        this.arrProduct = arrProduct;
        this.context = context;
    }
    @NonNull
    @Override
    public Product_OutOfStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new Product_OutOfStockViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_itemproductlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Product_OutOfStockViewHolder holder, int position) {
       if (arrProduct.size() > 0){
           ProductData  productData = arrProduct.get(position);
           getCategoryProduct_KetCategory(holder,position,productData);
           holder.btnDeleteProductItem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                Gọi hàm xóa sản phẩm
                   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setTitle("Thông báo");
                   builder.setMessage("Bạn có muốn xóa sản phẩm này không ?");
                   builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           deleteProduct(productData.getIdProduct(),holder);
                       }
                   });
                   builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                       }
                   });
                   AlertDialog alertDialog = builder.create();
                   alertDialog.show();
               }
           });
           holder.btnDetailProductItem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(context, Detailproduct.class);
                   intent.putExtra("informationProduct_Detail", productData);
                   context.startActivity(intent);
               }
           });
       }
    }
    //    hàm lấy category dựa vào key category của product
    private void getCategoryProduct_KetCategory(Product_OutOfStockViewHolder holder,int positon,ProductData productData){
        databaseReference = firebaseDatabase.getReference("Category");
        Query query = databaseReference.orderByChild("idCategory").equalTo(productData.getKeyCategoryProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot categoryItem : snapshot.getChildren()){
                        String idCategory = categoryItem.child("idCategory").getValue().toString();
                        String nameCategory = categoryItem.child("nameCategory").getValue().toString();
                        Category category = new Category(idCategory,idCategory,nameCategory);
                        getManuface_keyManufaceProduct(holder,positon,category,productData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    hàm xóa sản phẩm
    private void deleteProduct(String idProduct,Product_OutOfStockViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product");
        databaseReference.child(idProduct).removeValue();
        this.notifyDataSetChanged();
    }

    //    hàm lấy manuface theo key manuface của product
    private void getManuface_keyManufaceProduct( Product_OutOfStockViewHolder holder,int positon,Category category,ProductData productData){
        databaseReference = firebaseDatabase.getReference("Manuface");
        Query query = databaseReference.orderByChild("idManuface").equalTo(productData.getKeyManufaceProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot manufaceItem: snapshot.getChildren()){
                    String idManuface = manufaceItem.child("idManuface").getValue().toString();
                    String keyManuface_Category = manufaceItem.child("keyManuface_Category").getValue().toString();
                    String nameManuface = manufaceItem.child("nameManuface").getValue().toString();
                    Manuface manuface = new Manuface(idManuface,idManuface,nameManuface,keyManuface_Category);
                    setBackgroundItem(holder,positon,productData,category,manuface);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm tải lại giao diện product
    private void setBackgroundItem(Product_OutOfStockViewHolder holder,int position, ProductData productData, Category category, Manuface manuface) {
        holder.tvIdProductItem.setText(productData.getIdProduct());
        holder.tvNameProductItem.setText(productData.getNameProduct());
        holder.tvCategory.setText("Danh mục: " + category.getNameCategory());
        holder.tvManufaceProduct.setText("Hãng sản xuất: " + manuface.getNameManuface());
        holder.tvPriceProduct.setText("Giá: " + productData.getPriceProduct() + "VND");
        holder.tvQuanlityProduct.setText("Số lượng: " + productData.getQuanlityProduct());
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(productData.getIdProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot imageItem : snapshot.getChildren()){
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgProductItem);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.bg_item01);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_item02);
        }
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }
}
