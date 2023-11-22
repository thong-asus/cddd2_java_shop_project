package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.duancuahang.RecyclerView.Order_ListViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderListActivity extends AppCompatActivity {

    TabLayout tabLayout_ScreenOrderList;
    Toolbar toolBar_OrderList;
    ViewPager viewPager_ScreenOrderList;
    Order_ListViewPagerAdapter orderListViewPagerAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setControl();
        setIntiazation();
        context = this;
    }
    private void setControl(){
        tabLayout_ScreenOrderList = findViewById(R.id.tabLayout_ScreenOrderList);
        toolBar_OrderList = findViewById(R.id.toolBar_OrderList);
        viewPager_ScreenOrderList = findViewById(R.id.viewPager_ScreenOrderList);
    }
    private void setIntiazation() {
        orderListViewPagerAdapter = new Order_ListViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewPager_ScreenOrderList.setAdapter(orderListViewPagerAdapter);
        tabLayout_ScreenOrderList.setupWithViewPager(viewPager_ScreenOrderList);
        //Kích hoạt nút back
        setSupportActionBar(toolBar_OrderList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Integer activeTab = intent.getIntExtra("tabActive",0);
        tabLayout_ScreenOrderList.getTabAt(activeTab).select();
    }


    //set menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onPrepareOptionsMenu(menu);
    }
    //thêm menu vào action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        } else if (item.getItemId() == R.id.action_search){
            Intent intent = new Intent(OrderListActivity.this, SearchOrderActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_message){
            Intent intent = new Intent(context, ChatListActivity.class);
            //intent.putExtra("idUser","0372907720");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}