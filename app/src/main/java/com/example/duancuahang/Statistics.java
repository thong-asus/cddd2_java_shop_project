package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.RecyclerView.ItemProductStatistics_Adapter;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    Toolbar toolbarStatistics;
    RecyclerView rcvPRoductList;
    ItemProductStatistics_Adapter itemProductStatisticsAdapter;
    ArrayList<ProductData> arrayList = new ArrayList<>();
    Context context;
    ShopData shopData = new ShopData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        context = this;
        setControl();
        setIntiazation();
        getProduct();
    }

    private void getProduct() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+shopData.getIdShop());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot item:
                         snapshot.getChildren()) {
                        ProductData productData = item.getValue(ProductData.class);
                        arrayList.add(productData);
                        itemProductStatisticsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setIntiazation() {
        itemProductStatisticsAdapter = new ItemProductStatistics_Adapter(arrayList,context);
        rcvPRoductList.setLayoutManager(new LinearLayoutManager(context));
        rcvPRoductList.setAdapter(itemProductStatisticsAdapter);
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop", "");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        setSupportActionBar(toolbarStatistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        toolbarStatistics = findViewById(R.id.toolbarStatistics);
        rcvPRoductList = findViewById(R.id.rcvPRoductList);
    }
}