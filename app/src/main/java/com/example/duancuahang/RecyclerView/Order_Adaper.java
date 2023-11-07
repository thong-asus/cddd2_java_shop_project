package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Order_Adaper extends RecyclerView.Adapter<OrderItem_ViewHolder> {
    ArrayList<OrderData> arrayOrderData;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public Order_Adaper(ArrayList<OrderData> arrayOrderData, Context context){
        this.arrayOrderData = arrayOrderData;
        this.context = context;
        //loadOrderItem();
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
            holder.tvTotal.setText(orderData.getPrice_Order()+"VND");
            getInforCustomer(orderData.getIdCustomer_Order(),holder);
            getInforProduct(orderData.getIdProduct_Order(),holder);
            getImageProduct(orderData.getIdProduct_Order(),holder);
            System.out.println("ccccccccccccccccccccccccccccccc"+orderData.getIdCustomer_Order());

            if (position % 2 == 0){
                holder.itemView.setBackgroundResource(R.drawable.bg_item01);
            }
            else {
                holder.itemView.setBackgroundResource(R.drawable.bg_item02);
            }

            //Chuyển sang màn hình chi tiết đơn hàng
            holder.btnDetailOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                    Customer customer1 = snapshot.getValue(Customer.class);
                    holder.tvNameCustomer.setText(customer1.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImageProduct(String idProduct,OrderItem_ViewHolder holder){
        ArrayList<Image> arrImage = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot imageItem : snapshot.getChildren()){
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getInforProduct(String idProduct, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct.setText(productData.getNameProduct());
                    holder.tvPrice.setText(productData.getPriceProduct()+ "VND");

                    //Hiển thị hình ảnh sản phẩm
                    /////////////////////// CHƯA HIỂN THỊ ĐƯỢC HÌNH ẢNH
                    Image image = snapshot.getValue(Image.class);
                    Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Load Order Item
//    private void loadOrderItem(){
////        holder.tvNameCustomer.setText(orderData.getIdCustomer_Order());
////        holder.tvNameProduct.setText(orderData.getIdProduct_Order());
////        holder.tvAmountProduct.setText(orderData.getQuanlity_Order());
////        holder.tvTotal.setText(orderData.getPrice_Order());
//
//        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        databaseReference = firebaseDatabase.getReference("OrderPrduct/"+"0987653477");
////        Query query = databaseReference.orderByChild(orderData.getIdOrder());
////        query.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if(snapshot.exists()){
////                    for (DataSnapshot imageItem : snapshot.getChildren()){
////                        Image image = imageItem.getValue(Image.class);
////                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.ivProduct_ScreenCustomerItemProductList);
////                        return;
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    arrayOrderData.clear();
//                    for (DataSnapshot orderItem:
//                            snapshot.getChildren()) {
//                        OrderData orderData1 = orderItem.getValue(OrderData.class);
//                        if(orderData1.getStatusOrder() == 0){
//                            arrayOrderData.add(orderData1);
//                            System.out.println("order item: "+orderData1.toString());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return arrayOrderData.size();
    }
}
