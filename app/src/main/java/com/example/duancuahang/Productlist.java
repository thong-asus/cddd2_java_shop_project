package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.duancuahang.RecyclerView.ProductListViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Productlist extends AppCompatActivity {

    TabLayout tabLayout_ScreenProductList;
    ViewPager viewPager_ScreenProductList;
    ProductListViewPagerAdapter productListViewPagerAdapter;

    ImageView ivAddProduct_ProductList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        setControl();
        setIntiazation();
        setEvent();
        hildeKeyboard();
    }

    private void setEvent() {
        ivAddProduct_ProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("AddProduct", "ok");
            }
        });
    }

    private void setControl() {
        viewPager_ScreenProductList = findViewById(R.id.viewPager_ScreenProductList);
        tabLayout_ScreenProductList = findViewById(R.id.tabLayout_ScreenProductList);
        ivAddProduct_ProductList = findViewById(R.id.ivAddProduct_ProductList);
    }

    private void setIntiazation() {
        productListViewPagerAdapter = new ProductListViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewPager_ScreenProductList.setAdapter(productListViewPagerAdapter);
        tabLayout_ScreenProductList.setupWithViewPager(viewPager_ScreenProductList);
    }

    private void hildeKeyboard(){
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}