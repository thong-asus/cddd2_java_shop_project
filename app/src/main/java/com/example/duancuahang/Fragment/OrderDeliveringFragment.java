package com.example.duancuahang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.OrderItem_Adaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class OrderDeliveringFragment extends Fragment {

    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderDelivering;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adaper orderAdaper;
    RecyclerView rcvOrderDelivering;
    ShopData shopData = new ShopData();
    private String shopPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_delivering, container, false);
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
        orderAdaper = new OrderItem_Adaper(arrayOrderData, getContext());
        rcvOrderDelivering.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderDelivering.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderDelivering = view.findViewById(R.id.rcvOrderDelivering);
        tvNoOrderDelivering = view.findViewById(R.id.tvNoOrderDelivering);
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
                        if (orderData1.getStatusOrder() == 2) {
                            arrayOrderData.add(orderData1);
                            //System.out.println("order item: " + orderData1.toString());
                        }
                    }
                }
                if (arrayOrderData.size() <= 0) {
                    tvNoOrderDelivering.setVisibility(View.VISIBLE);
                    rcvOrderDelivering.setVisibility(View.GONE);
                } else {
                    tvNoOrderDelivering.setVisibility(View.GONE);
                    rcvOrderDelivering.setVisibility(View.VISIBLE);
                }
                orderAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}