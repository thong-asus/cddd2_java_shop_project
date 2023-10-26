package com.example.duancuahang.Fragment;

import android.content.Context;
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
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.Product_OutOfStockAdater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductisoutofstockFragment extends Fragment {

    RecyclerView rcvProductisoutstock_ScreenProductList;
    TextView tvNoProduct_OutOfStock;
    Product_OutOfStockAdater productOutOfStockAdater;
    ArrayList<ProductData> arrProducts = new ArrayList<>();
    DatabaseReference databaseReference;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productisoutofstock,container,false);
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product");
        Query query = databaseReference.orderByChild("quanlityProduct").equalTo(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrProducts.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()){
                    String idProduct = productSnapshot.child("idProduct").getValue().toString();
                    String nameProduct = productSnapshot.child("nameProduct").getValue().toString();
                    String urlImageProduct = productSnapshot.child("urlImageProduct").getValue().toString();
                    int priceProduct = Integer.parseInt(productSnapshot.child("priceProduct").getValue().toString());
                    String keyCategoryProduct = productSnapshot.child("keyCategoryProduct").getValue().toString();
                    String keyManufaceProduct = productSnapshot.child("keyManufaceProduct").getValue().toString();
                    String descriptionProduct = productSnapshot.child("descriptionProduct").getValue().toString();
                    int quanlityProduct = Integer.parseInt(productSnapshot.child("quanlityProduct").getValue().toString());
                    int sumLike = Integer.parseInt(productSnapshot.child("sumLike").getValue().toString());
                    double overageCmtProduct = Double.parseDouble(productSnapshot.child("overageCmtProduct").getValue().toString());
                    ProductData productData = new ProductData(idProduct,nameProduct,urlImageProduct,priceProduct,keyCategoryProduct,keyManufaceProduct,quanlityProduct,descriptionProduct,sumLike,overageCmtProduct);
                    arrProducts.add(productData);
                }
                productOutOfStockAdater.notifyDataSetChanged();
                if (arrProducts.size() <=0){
                    tvNoProduct_OutOfStock.setVisibility(View.VISIBLE);
                    rcvProductisoutstock_ScreenProductList.setVisibility(View.GONE);
                }
                else {
                    tvNoProduct_OutOfStock.setVisibility(View.GONE);
                    rcvProductisoutstock_ScreenProductList.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
