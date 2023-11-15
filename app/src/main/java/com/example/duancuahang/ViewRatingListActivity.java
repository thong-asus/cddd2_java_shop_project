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
import com.example.duancuahang.Fragment.ViewCommentProductFragment;
import com.example.duancuahang.RecyclerView.ViewRatingItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewRatingListActivity extends AppCompatActivity {


    Toolbar toolbar_Rating;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ViewRatingItemAdapter viewRatingItemAdapter;
    RecyclerView rcvListItemRating;
    private ShopData shopData = new ShopData();
    ArrayList<ProductData> arrayProductData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewratingproduct);

        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);

        setControl();
        setIntiazation();
        //Lấy tất cả sản phẩm
        getAllProduct();
    }

    private void setIntiazation() {
        viewRatingItemAdapter = new ViewRatingItemAdapter(arrayProductData, this);
        rcvListItemRating.setLayoutManager(new LinearLayoutManager(this));
        rcvListItemRating.setAdapter(viewRatingItemAdapter);
        viewRatingItemAdapter.notifyDataSetChanged();

        setSupportActionBar(toolbar_Rating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getAllProduct() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product");
        Query query = databaseReference.orderByChild("idUserProduct").equalTo(shopData.getIdShop());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayProductData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        ProductData productData = productSnapshot.getValue(ProductData.class);
                        arrayProductData.add(productData);
                    }
                }
                viewRatingItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy sản phẩm");
            }
        });
    }
    private void setControl() {
        rcvListItemRating = findViewById(R.id.rcvListItemRating);
        toolbar_Rating = findViewById(R.id.toolbar_Rating);
    }
}