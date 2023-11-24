package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.Voucher;
import com.example.duancuahang.RecyclerView.VoucherItem_Adapter;
import com.example.duancuahang.RecyclerView.VoucherPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class MyVoucherActivity extends AppCompatActivity {
    Context context;
    Toolbar toolbar_Voucher;
    ImageView imgAddNewVoucher;
    RecyclerView rcvMyVoucher_Shop;
    ArrayList<Voucher> arrVoucher = new ArrayList<>();
    VoucherItem_Adapter voucherItemAdapter;
    private ShopData shopData = new ShopData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher);
        context = this;
        setControl();
        setIntiazation();
        getAllVoucherOfShop();
        setEvent();
    }

//    hàm lấy voucher của shop
    private void getAllVoucherOfShop(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Voucher/"+shopData.getIdShop());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot itemProduct:
                         snapshot.getChildren()) {
                        for (DataSnapshot itemVoucherOfProduct:
                             itemProduct.getChildren()) {
                            Voucher itemVoucher = itemVoucherOfProduct.getValue(Voucher.class);
                            arrVoucher.add(itemVoucher);
                            voucherItemAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        //Chuyển sang màn hình tạo Voucher
        imgAddNewVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyVoucherActivity.this, AddNewVoucherActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setIntiazation() {
        //Kích hoạt nút back
        setSupportActionBar(toolbar_Voucher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        gán giá trị chi voucher item adapter
        voucherItemAdapter = new VoucherItem_Adapter(arrVoucher,context);
        rcvMyVoucher_Shop.setLayoutManager(new LinearLayoutManager(context));
        rcvMyVoucher_Shop.setAdapter(voucherItemAdapter);

//        lấy thông tin shop lưu tong Ref
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop", "");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setControl() {
        toolbar_Voucher = findViewById(R.id.toolbar_Voucher);
        imgAddNewVoucher = findViewById(R.id.imgAddNewVoucher);
        rcvMyVoucher_Shop = findViewById(R.id.rcvMyVoucher_Shop);
    }
}