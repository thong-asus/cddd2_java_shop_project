package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.ItemMessage;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.MessageActivity;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemPeopleMessage_Adapter extends RecyclerView.Adapter<ItemPropleMessage_ViewHolder> {
    ArrayList<String> arrIdShopMess = new ArrayList<>();
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private ShopData shopData = new ShopData();

    public ItemPeopleMessage_Adapter(ArrayList<String> arrIdShopMessage, Context context) {
        this.arrIdShopMess = arrIdShopMessage;
        this.context = context;
        ////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences = context.getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (sharedPreferences.contains("informationShop")) {
            String jsonShop = sharedPreferences.getString("informationShop", "");
            Gson gson = new Gson();
            shopData = gson.fromJson(jsonShop, ShopData.class);
        } else {
            // Dữ liệu không tồn tại, có thể là người dùng đã đăng xuất hoặc lần đầu sử dụng ứng dụng
        }
        ////////////////////////////////////////////////////////////////////////////////////////

    }

    @NonNull
    @Override
    public ItemPropleMessage_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemPropleMessage_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_peoplemessage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPropleMessage_ViewHolder holder, int position) {
        String idCustomer = arrIdShopMess.get(position);
        setBackgroundInformationShopMessage(idCustomer, holder);
        contentMessage(idCustomer, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("idUser",idCustomer);
                context.startActivity(intent);
            }
        });
    }

    private void contentMessage(String idCustomer, ItemPropleMessage_ViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("Message/" + shopData.getIdShop() + "/" + idCustomer);
        databaseReference.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item:
                         snapshot.getChildren()) {
                        databaseReference = firebaseDatabase.getReference("ItemMessage/"+item.getKey().toString());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ItemMessage itemMessage = snapshot.getValue(ItemMessage.class);
                                String nameTable = itemMessage.getIdSender().split("-")[1];
                                if (nameTable.equals("Customer")){
                                    holder.tvContent_CustomerItemUserMessage.setText(itemMessage.getContentMessage());
                                }
                                else {
                                    holder.tvContent_CustomerItemUserMessage.setText("Bạn: "+itemMessage.getContentMessage());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setBackgroundInformationShopMessage(String idCustomer, ItemPropleMessage_ViewHolder holder) {
        System.out.println("asdf: " + idCustomer);
        databaseReference = firebaseDatabase.getReference("Customer/" + idCustomer);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    ShopData shopData = snapshot.getValue(ShopData.class);
                    Customer customer = snapshot.getValue(Customer.class);
//                    System.out.println("s: " + shopData.toString());
                    if (customer.getImageUser().isEmpty()) {
                        holder.ivAvataUser_CustomerItemUserMessage.setImageResource(R.drawable.ic_chat);
                    } else {
                        Picasso.get().load(customer.getImageUser()).into(holder.ivAvataUser_CustomerItemUserMessage);
                    }
                    holder.tvNameUser_CustomerItemUserMessage.setText(customer.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrIdShopMess.size();
    }
}
