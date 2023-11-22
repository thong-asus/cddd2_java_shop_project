package com.example.duancuahang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.RecyclerView.Product_InOfStockAdapter;
import com.example.duancuahang.Class.Product;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductisinofstockFragment extends Fragment {
    RecyclerView rcvProductisinstock_ScreenPRoductList;
    TextView tvNoProduct_InOfStock;
    Product_InOfStockAdapter productInOfStockAdapter;
    ArrayList<ProductData> arrProductData = new ArrayList<>();
    DatabaseReference databaseReference;

    ShopData shopData = new ShopData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productisinofstock, container, false);
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop", "");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        setControl(view);
        setIntiazation();
        setEvent();
        pullProduct_IsInofStock();
        return view;
    }

    private void setIntiazation() {
        productInOfStockAdapter = new Product_InOfStockAdapter(arrProductData, getContext());
        rcvProductisinstock_ScreenPRoductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProductisinstock_ScreenPRoductList.setAdapter(productInOfStockAdapter);
        productInOfStockAdapter.notifyDataSetChanged();
    }

    //    hàm lấy danh sách sản phẩm còn hàng
    private void pullProduct_IsInofStock() {

        System.out.println("id shop: " + shopData.getIdShop());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product/" + shopData.getIdShop());
//        Query query =  databaseReference.orderByChild("idUserProduct").equalTo(shopData.getIdShop());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrProductData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        ProductData productData = productSnapshot.getValue(ProductData.class);
                        if (productData.getQuanlityProduct() > 0) {
                            arrProductData.add(productData);
                        }
                    }
                }

                if (arrProductData.size() <= 0) {
                    tvNoProduct_InOfStock.setVisibility(View.VISIBLE);
                    rcvProductisinstock_ScreenPRoductList.setVisibility(View.GONE);
                } else {
                    tvNoProduct_InOfStock.setVisibility(View.GONE);
                    rcvProductisinstock_ScreenPRoductList.setVisibility(View.VISIBLE);
                }
                productInOfStockAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("loi lay san pham");
            }
        });

    }

    private void setEvent() {


    }

    private void setControl(View view) {
        rcvProductisinstock_ScreenPRoductList = view.findViewById(R.id.rcvProductisinstock_ScreenPRoductList);
        tvNoProduct_InOfStock = view.findViewById(R.id.tvNoProduct_InOfStock);
    }


}
