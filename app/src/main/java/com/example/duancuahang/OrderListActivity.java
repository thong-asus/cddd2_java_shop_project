package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        setEvent();
        context = this;
    }
    private void setEvent(){

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
            Toast.makeText(context, "Chuyển sang màn hình tìm kiếm", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_message){
            Toast.makeText(context, "Chuyển sang màn hình tin nhắn", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}