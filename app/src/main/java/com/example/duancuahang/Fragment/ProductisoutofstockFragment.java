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
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.Product_OutOfStockAdater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProductisoutofstockFragment extends Fragment {

    RecyclerView rcvProductisoutstock_ScreenProductList;
    TextView tvNoProduct_OutOfStock;
    Product_OutOfStockAdater productOutOfStockAdater;
    ArrayList<ProductData> arrProducts = new ArrayList<>();
    DatabaseReference databaseReference;
    Context context;
    ShopData shopData = new ShopData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productisoutofstock,container,false);
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        setControl(view);
        setIntiazation();
        setEvent();
        pullProductQuanlityIs_0();
        return view;
    }

    private void setIntiazation() {
        productOutOfStockAdater = new Product_OutOfStockAdater(arrProducts,getContext());
        rcvProductisoutstock_ScreenProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProductisoutstock_ScreenProductList.setAdapter(productOutOfStockAdater);
        productOutOfStockAdater.notifyDataSetChanged();
    }

    private void setEvent() {
    }

    private void setControl(View view) {
        rcvProductisoutstock_ScreenProductList = view.findViewById(R.id.rcvProductisoutstock_ScreenProductList);
        tvNoProduct_OutOfStock = view.findViewById(R.id.tvNoProduct_OutOfStock);
    }

    private void pullProductQuanlityIs_0(){
        System.out.println("id shop: " + shopData.getIdShop());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product");
        Query query =  databaseReference.orderByChild("idUserProduct").equalTo(shopData.getIdShop());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrProducts.clear();
                if(snapshot.exists()){
                    for (DataSnapshot productSnapshot : snapshot.getChildren()){
                        ProductData productData = productSnapshot.getValue(ProductData.class);
                        if (productData.getQuanlityProduct() <= 0){
                            arrProducts.add(productData);
                        }
                    }
                }
                else {

                    if (arrProducts.size() <= 0){
                        tvNoProduct_OutOfStock.setVisibility(View.VISIBLE);
                        rcvProductisoutstock_ScreenProductList.setVisibility(View.GONE);
                    }
                    else {
                        tvNoProduct_OutOfStock.setVisibility(View.GONE);
                        rcvProductisoutstock_ScreenProductList.setVisibility(View.VISIBLE);
                    }
                }
                productOutOfStockAdater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("loi lay san pham");
            }
        });
    }
}
