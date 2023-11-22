package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.duancuahang.RecyclerView.VoucherPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MyVoucherActivity extends AppCompatActivity {
    Context context;
    Toolbar toolbar_Voucher;
    ViewPager viewPager_ScreenVoucher;
    TabLayout tabLayout_ScreenVoucher;
    ImageView imgAddNewVoucher;
    VoucherPagerAdapter voucherViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher);
        context = this;
        setControl();
        setIntiazation();
    }



    private void setIntiazation() {
        voucherViewPagerAdapter = new VoucherPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewPager_ScreenVoucher.setAdapter(voucherViewPagerAdapter);
        tabLayout_ScreenVoucher.setupWithViewPager(viewPager_ScreenVoucher);

        //Chuyển sang màn hình tạo Voucher
        imgAddNewVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyVoucherActivity.this, AddNewVoucherActivity.class);
                startActivity(intent);
            }
        });
        //Kích hoạt nút back
        setSupportActionBar(toolbar_Voucher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        tabLayout_ScreenVoucher = findViewById(R.id.tabLayout_ScreenVoucher);
        viewPager_ScreenVoucher = findViewById(R.id.viewPager_ScreenVoucher);
    }
}