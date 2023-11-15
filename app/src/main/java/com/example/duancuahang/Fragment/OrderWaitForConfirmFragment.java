package com.example.duancuahang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.OrderItem_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class OrderWaitForConfirmFragment extends Fragment {
    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderWaitConfirm;
    DatabaseReference databaseReference;
    //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseDatabase firebaseDatabase;
   OrderItem_Adapter orderAdaper;
   RecyclerView rcvOrderWaitConfirm;
    private ShopData shopData = new ShopData();
    private String shopPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_wait_for_confirm, container, false);
        ////////////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop", "");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        ////////////////////////////////////////////////////////////////////////////////////////////////
        loadOrderItem();
        setControl(view);
        setInitiazation();
        setEvent();
        // Inflate the layout for this fragment
        return view;
    }

    private void setInitiazation() {
        orderAdaper = new OrderItem_Adapter(arrayOrderData, getContext());
        rcvOrderWaitConfirm.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderWaitConfirm.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderWaitConfirm = view.findViewById(R.id.rcvOrderWaitConfirm);
        tvNoOrderWaitConfirm = view.findViewById(R.id.tvNoOrderWaitConfirm);
    }

    private void loadOrderItem(){
        String fullPath = "OrderProduct/" + shopData.getIdShop();
        databaseReference = firebaseDatabase.getInstance().getReference(fullPath);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayOrderData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot orderItem : snapshot.getChildren()) {
                        OrderData orderData1 = orderItem.getValue(OrderData.class);
                        if (orderData1.getStatusOrder() == 0) {
                            arrayOrderData.add(orderData1);
                            //System.out.println("order item: " + orderData1.toString());
                        }
                    }
                }
                if (arrayOrderData.size() <= 0) {
                    tvNoOrderWaitConfirm.setVisibility(View.VISIBLE);
                    rcvOrderWaitConfirm.setVisibility(View.GONE);
                } else {
                    tvNoOrderWaitConfirm.setVisibility(View.GONE);
                    rcvOrderWaitConfirm.setVisibility(View.VISIBLE);
                }
                orderAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}