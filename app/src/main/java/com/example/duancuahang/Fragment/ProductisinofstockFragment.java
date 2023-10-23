package com.example.duancuahang.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.RecyclerView.Product_InOfStockAdapter;
import com.example.duancuahang.Class.Product;
import com.example.duancuahang.R;

import java.util.ArrayList;
import java.util.List;

public class ProductisinofstockFragment extends Fragment {
    RecyclerView rcvProductisinstock_ScreenPRoductList;
    Product_InOfStockAdapter productInOfStockAdapter;
    List<Product> products = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productisinofstock,container,false);
        setControl(view);
        setIntiazation();
        setEvent();
        return view;
    }

    private void setIntiazation() {
        Product product = new Product("ma1","Sp1",null,12,1,1,1,1,12,2.3);
        Product product1 = new Product("ma1","Sp1",null,12,1,1,1,1,12,2.3);
        Product product2 = new Product("ma1","Sp1",null,12,1,1,1,1,12,2.3);
        products.add(product1);
        products.add(product2);
        products.add(product);
    }

    private void setEvent() {
        productInOfStockAdapter = new Product_InOfStockAdapter(products,getContext());
        rcvProductisinstock_ScreenPRoductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProductisinstock_ScreenPRoductList.setAdapter(productInOfStockAdapter);
        productInOfStockAdapter.notifyDataSetChanged();

    }

    private void setControl(View view) {
        rcvProductisinstock_ScreenPRoductList = view.findViewById(R.id.rcvProductisinstock_ScreenPRoductList);
    }


}
