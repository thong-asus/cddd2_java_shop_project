package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.FormatMoneyVietNam;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Detailproduct;
import com.example.duancuahang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Product_InOfStockAdapter extends RecyclerView.Adapter<Product_InOfStockViewHolder> {

    ArrayList<ProductData> products;
    Context context;
    DatabaseReference databaseReference;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRefTransaction = firebaseDatabase.getReference();
    private  boolean loadingData = false;


    public Product_InOfStockAdapter(ArrayList<ProductData> products, Context context){
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public Product_InOfStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Product_InOfStockViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_itemproductlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Product_InOfStockViewHolder holder, int position) {
        if (products.size() > 0){
            ProductData product = products.get(position);
            setInformationProduct_Item(holder,product);
            setCategoryProduct_KetCategory(holder,product);
            setManuface_keyManufaceProduct(holder,product);
            if (position % 2 == 0){
                holder.itemView.setBackgroundResource(R.drawable.bg_item01);
            }
            else {
                holder.itemView.setBackgroundResource(R.drawable.bg_item02);
            }
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
                            deleteProduct(product,holder);
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
                    intent.putExtra("informationProduct_Detail", product);
                    context.startActivity(intent);
                }
            });
        }
    }

//    hàm lấy category dựa vào key category của product
    private void setCategoryProduct_KetCategory(Product_InOfStockViewHolder holder, ProductData productData){

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
    private void setManuface_keyManufaceProduct(Product_InOfStockViewHolder holder, ProductData productData){
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
                    holder.tvManufaceProduct.setText(manuface.getNameManuface());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    hàm tải lại giao diện product
private void setInformationProduct_Item(Product_InOfStockViewHolder holder, ProductData productData){
    holder.tvIdProductItem.setText(productData.getIdProduct());
    holder.tvNameProductItem.setText(productData.getNameProduct());
    holder.tvPriceProduct.setText("Giá: " + FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct()) + "đ");
    holder.tvQuanlityProduct.setText("Số lượng: " + productData.getQuanlityProduct());

    databaseReference = firebaseDatabase.getReference("ImageProducts/"+productData.getIdProduct()+"/1");
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
               String url = snapshot.child("urlImage").getValue().toString();
               Picasso.get().load(url).placeholder(R.drawable.icondowload).into(holder.imgProductItem);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Xử lý lỗi nếu có
        }
    });
}


    //    hàm xóa sản phẩm dựa vào id product
    private void deleteProduct(ProductData productData,Product_InOfStockViewHolder holder){
        databaseReference = firebaseDatabase.getReference("OrderProduct");
        Query query = databaseReference.orderByChild("idProduct_Order").equalTo(productData.getIdProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Sản phẩm đang trong thời kỳ giao dịch. Vui lòng kết thúc giao dịch trước khi xóa");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    databaseReference = firebaseDatabase.getReference("Product/"+productData.getIdUserProduct()+"/"+productData.getIdProduct());
                    databaseReference.removeValue().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ShowMessage.showMessageTimer(context,"Xóa sản phẩm thất bại. Vui lòng kiểm tra lại");
                        }
                    })
                    ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    @Override
    public int getItemCount() {
        return products.size();
    }

}
