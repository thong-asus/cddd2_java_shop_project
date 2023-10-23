package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class HomeShop extends AppCompatActivity {

    Toolbar toolbar;

    LinearLayout linearLayout_SanPhamCuaToi_ScreenHome;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shop);
        setControl();
        setEvent();
        setSupportActionBar(toolbar);
        context = this;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onPrepareOptionsMenu(menu);
    }

//    Xu ly su kien
    private void setEvent() {
        linearLayout_SanPhamCuaToi_ScreenHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Productlist.class);
                context.startActivity(intent);
            }
        });

    }

//    Anh xa
    private void setControl() {
        toolbar = findViewById(R.id.toolBar_ScreenHome);
        linearLayout_SanPhamCuaToi_ScreenHome = findViewById(R.id.linearLayout_SanPhamCuaToi_ScreenHome);
    }

//    Thêm item và ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}