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
import com.example.duancuahang.RecyclerView.OrderItem_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;


public class OrderDeliveredFragment extends Fragment {

    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderDelivered;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adapter orderAdaper;
    RecyclerView rcvOrderDelivered;
    private ShopData shopData = new ShopData();
    private String shopPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_delivered, container, false);
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
        rcvOrderDelivered.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderDelivered.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderDelivered = view.findViewById(R.id.rcvOrderDelivered);
        tvNoOrderDelivered = view.findViewById(R.id.tvNoOrderDelivered);
    }

    private void loadOrderItem() {
        databaseReference = firebaseDatabase.getReference("OrderShop/"+shopData.getIdShop());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayOrderData.clear();
                if(snapshot.exists()){
                    for (DataSnapshot itemIdOrder:
                            snapshot.getChildren()) {
                        String idOrderItem = itemIdOrder.getKey();
                        System.out.println("id order: "+idOrderItem);
                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("OrderProduct/"+idOrderItem);
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    OrderData orderData = snapshot.getValue(OrderData.class);
                                    if (orderData.getStatusOrder() == 4){
                                        arrayOrderData.add(orderData);
                                    }
                                    else {
                                        arrayOrderData.removeIf(element->element.getIdOrder().equals(orderData.getIdOrder()));
                                    }
                                    Collections.reverse(arrayOrderData);
                                    orderAdaper.notifyDataSetChanged();

                                    if (arrayOrderData.size() <= 0) {
                                        tvNoOrderDelivered.setVisibility(View.VISIBLE);
                                        rcvOrderDelivered.setVisibility(View.GONE);
                                    } else {
                                        tvNoOrderDelivered.setVisibility(View.GONE);
                                        rcvOrderDelivered.setVisibility(View.VISIBLE);
                                    }
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
}