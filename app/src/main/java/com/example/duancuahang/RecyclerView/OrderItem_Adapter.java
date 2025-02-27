package com.example.duancuahang.RecyclerView;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.FormatMoneyVietNam;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.Shop;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.OrderDetailActivity;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderItem_Adapter extends RecyclerView.Adapter<OrderItem_ViewHolder> {
    FrameLayout frameLayout_ItemOrderList;
    ArrayList<OrderData> arrayOrderData;
    Context context;
    ShopData shopData = new ShopData();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public void setData(ArrayList<OrderData> arrayOrderData) {
        this.arrayOrderData = arrayOrderData;
    }

    public OrderItem_Adapter(ArrayList<OrderData> arrayOrderData, Context context){
        this.arrayOrderData = arrayOrderData;
        this.context = context;
        //loadOrderItem();
        getInforShop();

    }
    private void getInforShop(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
    }

    @NonNull
    @Override
    public OrderItem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItem_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_orderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItem_ViewHolder holder, int position) {
        if(arrayOrderData.size()>0){
            OrderData orderData = arrayOrderData.get(position);
            holder.tvAmountProduct.setText(orderData.getQuanlity_Order()+"");
            holder.tvTotal.setText(FormatMoneyVietNam.formatMoneyVietNam(orderData.getPrice_Order())+"đ");
            getInforCustomer(orderData.getIdCustomer_Order(),holder);
            getInforProduct(orderData.getIdProduct_Order(),holder);
            getImageProduct(orderData.getIdProduct_Order(),holder);
            //System.out.println("ccccccccccccccccccccccccccccccc"+orderData.getIdCustomer_Order());

            if (position % 2 == 0){
                holder.itemView.setBackgroundResource(R.drawable.bg_item01);
            }
            else {
                holder.itemView.setBackgroundResource(R.drawable.bg_item02);
            }

            //Chuyển sang màn hình chi tiết đơn hàng
            final int finalPosition = position;
            holder.linearLayout_ItemOrderList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    /////////Truyền dữ liệu qua màn hình order detail///////////
                    OrderData orderData1 = arrayOrderData.get(finalPosition);
                    intent.putExtra("orderData1", orderData1);

                    System.out.println("Dữ liệu truyền đi tại OrderItem: "+orderData1);
                    ////////////////////////////////////////////////////////////
                    context.startActivity(intent);
                }
            });
        }
    }
    private void getInforCustomer(String idCustomer, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Customer/"+idCustomer);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    Customer customer1 = snapshot.getValue(Customer.class);
//                    holder.tvNameCustomer.setText(customer1.getName());
                    String name = snapshot.child("name").getValue(String.class);
                    holder.tvNameCustomer.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImageProduct(String idProduct, OrderItem_ViewHolder holder) {
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
                            Picasso.get().load(imageUrl).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
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

    private void getInforProduct(String idProduct, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+shopData.getIdShop()+"/"+idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct.setText(productData.getNameProduct());
                    holder.tvPrice.setText(FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct())+ "đ");

                    //Hiển thị hình ảnh sản phẩm
//                    Image image = snapshot.getValue(Image.class);
//                    Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayOrderData.size();
    }
}
