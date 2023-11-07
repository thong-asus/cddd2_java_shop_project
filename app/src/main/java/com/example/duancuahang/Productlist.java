package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.duancuahang.RecyclerView.ProductListViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Productlist extends AppCompatActivity {

    TabLayout tabLayout_ScreenProductList;
    ViewPager viewPager_ScreenProductList;
    Toolbar toolBar_ProductList;
    ImageView ivAddProduct_ProductList;
    ProductListViewPagerAdapter productListViewPagerAdapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        context = this;
        setControl();
        setIntiazation();
        setEvent();
        hildeKeyboard();
    }

    private void setEvent() {
        ivAddProduct_ProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Addproduct.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        viewPager_ScreenProductList = findViewById(R.id.viewPager_ScreenProductList);
        tabLayout_ScreenProductList = findViewById(R.id.tabLayout_ScreenProductList);
        ivAddProduct_ProductList = findViewById(R.id.ivAddProduct_ProductList);
        toolBar_ProductList = findViewById(R.id.toolBar_ProductList);
    }

    private void setIntiazation() {

        productListViewPagerAdapter = new ProductListViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewPager_ScreenProductList.setAdapter(productListViewPagerAdapter);
        tabLayout_ScreenProductList.setupWithViewPager(viewPager_ScreenProductList);

//        ---------------- kích hoạt button back của toolbar
        setSupportActionBar(toolBar_ProductList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    bắt sự kiện nhấn vào nút back của toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hildeKeyboard(){
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}