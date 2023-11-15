package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duancuahang.Class.MessageData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Message_Adapter extends RecyclerView.Adapter<Message_ViewHolder> {
    private ArrayList<MessageData> arrMessage = new ArrayList<>();
    private Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public Message_Adapter(ArrayList<MessageData> arrMessage, Context context){
        this.arrMessage = arrMessage;
        this.context = context;
    }
    @NonNull
    @Override
    public Message_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Message_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_message,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Message_ViewHolder holder, int position) {
        MessageData messageData = arrMessage.get(position);
        String nameTable = messageData.getIdSender().toString().split("-")[1];
        String idUser = messageData.getIdSender().toString().split("-")[0];
        System.out.println("nameTB: "+ nameTable);

        if (nameTable.equals("Shop")){
            setInformationShop(idUser,holder);
            holder.tvContentItem_CustomerItemMessage.setText(messageData.getContentMessage());
            holder.vItemMessage.setGravity(Gravity.RIGHT);
            holder.ivAvataUser_CustomerItemMessage.setVisibility(View.VISIBLE);
            holder.tvContentItem_CustomerItemMessage.setBackgroundResource(R.drawable.bg_tvcontent_user01);
        }
        else if (nameTable.equals("Customer")){
            holder.ivAvataUser_CustomerItemMessage.setVisibility(View.GONE);
            holder.tvContentItem_CustomerItemMessage.setText(messageData.getContentMessage());
            holder.tvContentItem_CustomerItemMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.vItemMessage.setGravity(Gravity.LEFT);
            holder.tvContentItem_CustomerItemMessage.setBackgroundResource(R.drawable.bg_tvcontent_user02);
        }
    }
    private void setInformationShop(String idShop,Message_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Shop/" +idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                   if (shopData.getUrlImgShopAvatar().isEmpty()){
                       Picasso.get().load(R.drawable.iconshop).into(holder.ivAvataUser_CustomerItemMessage);
                   }else {
                       Picasso.get().load(shopData.getUrlImgShopAvatar()).into(holder.ivAvataUser_CustomerItemMessage);
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrMessage.size();
    }
}
