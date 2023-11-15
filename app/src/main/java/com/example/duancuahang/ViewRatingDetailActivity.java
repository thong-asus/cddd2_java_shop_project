package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Fragment.ViewLikeProductFragment;
import com.example.duancuahang.RecyclerView.Order_ListViewPagerAdapter;
import com.example.duancuahang.RecyclerView.ViewCommentedDetailItemAdapter;
import com.example.duancuahang.RecyclerView.ViewLikedDetailItemAdapter;
import com.example.duancuahang.RecyclerView.ViewRatingDetailPagerAdapter;
import com.example.duancuahang.RecyclerView.ViewRatingItemAdapter;
import com.google.android.material.tabs.TabLayout;

public class ViewRatingDetailActivity extends AppCompatActivity {


    androidx.appcompat.widget.Toolbar toolBar_RatingDetail;
    TextView tvNameProductRated;
    TabLayout tabLayout_ScreenRatingDetail;
    ViewPager viewPager_ScreenRatingDetail;
    View vRatingDetail;
    Context context;
    ViewRatingDetailPagerAdapter viewRatingDetailPagerAdapter;
//    ViewLikedDetailItemAdapter viewLikedDetailItemAdapter;
//    ViewCommentedDetailItemAdapter viewCommentedDetailItemAdapter;
    ProductData productData = new ProductData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rating_detail);


        /////////////////////////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productData1")) {
            productData = (ProductData) intent.getSerializableExtra("productData1");
            if (productData != null) {
                System.out.println("Dữ liệu PRODUCT DATA nhận được tại ViewRatingDetailActivity: " + productData);
            }
        }

        /////////////////////////////////////////////////////////////////////////////////////
        setControl();
        setIntiazation();
    }

    private void setControl() {
        toolBar_RatingDetail = findViewById(R.id.toolBar_RatingDetail);
        tvNameProductRated = findViewById(R.id.tvNameProductRated);
        tabLayout_ScreenRatingDetail = findViewById(R.id.tabLayout_ScreenRatingDetail);
        viewPager_ScreenRatingDetail = findViewById(R.id.viewPager_ScreenRatingDetail);
        vRatingDetail = findViewById(R.id.vRatingDetail);
    }

    private void setIntiazation() {
        viewRatingDetailPagerAdapter = new ViewRatingDetailPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewRatingDetailPagerAdapter.setProduct(productData.getIdProduct());
        viewPager_ScreenRatingDetail.setAdapter(viewRatingDetailPagerAdapter);
        tabLayout_ScreenRatingDetail.setupWithViewPager(viewPager_ScreenRatingDetail);

        //////////////////LẤY TÊN SẢN PHẨM/////////////////////////
        tvNameProductRated.setText(productData.getNameProduct());

        setSupportActionBar(toolBar_RatingDetail);
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
}