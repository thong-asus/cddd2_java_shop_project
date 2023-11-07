package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Fragment.OrderWaitForTakeGoodsFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class HomeShop extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linearLayout_SanPhamCuaToi_ScreenHome;
    View frChoLayHang, frHuyDonHang, frDanhGia;
    TextView tvNameShop_ScreenHome, tvBillHistory;
    ImageView ivAvataShop_ScreenHome;
    Context context;
    private ShopData shopData = new ShopData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shop);




        SharedPreferences sharedPreferences = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (sharedPreferences.contains("informationShop")) {
            String jsonShop = sharedPreferences.getString("informationShop", "");
            Gson gson = new Gson();
            shopData = gson.fromJson(jsonShop, ShopData.class);
        } else {
            // Dữ liệu không tồn tại, có thể là người dùng đã đăng xuất hoặc lần đầu sử dụng ứng dụng
        }


//        if (isLoggedIn) {
//            setContentView(R.layout.activity_home_shop);
//        } else {
//            Intent intent = new Intent(HomeShop.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        System.out.println("Shopdata Share: " + shopData.toString());


        context = this;
        setControl();
        setInitialization();
        setEvent();
        setSupportActionBar(toolbar);

    }

    private void setInitialization() {
            tvNameShop_ScreenHome.setText(shopData.getShopName());
        if (shopData.getUrlImgShopAvatar().isEmpty()){
            ivAvataShop_ScreenHome.setImageResource(R.drawable.iconshop);
        }
        else {
            Picasso.get().load(shopData.getUrlImgShopAvatar()).into(ivAvataShop_ScreenHome);
        }
    }

    //set menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onPrepareOptionsMenu(menu);
    }

    //xử lý sự kiện khi chọn menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.itSetting_Actionbar){
                Intent intent = new Intent(HomeShop.this, InformationAccountActivity.class);
                startActivity(intent);
                Toast.makeText(context, "Chuyển sang màn hình Cài đặt", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.itNotification_Actionbar){
            Toast.makeText(context, "Chuyển sang màn hình Thông báo", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.itMessage_Actionbar){
            Toast.makeText(context, "Chuyển sang màn hình Tin nhắn", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
//    Xu ly su kien
    private void setEvent() {
        linearLayout_SanPhamCuaToi_ScreenHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Productlist.class);
                startActivity(intent);
            }
        });
        tvBillHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeShop.this,OrderListActivity.class);
                startActivity(intent);
            }
        });

    }

//    Anh xa
    private void setControl() {
        toolbar = findViewById(R.id.toolBar_ScreenHome);
        linearLayout_SanPhamCuaToi_ScreenHome = findViewById(R.id.linearLayout_SanPhamCuaToi_ScreenHome);
        tvNameShop_ScreenHome = findViewById(R.id.tvNameShop_ScreenHome);
        tvBillHistory = findViewById(R.id.tvBillHistory);
        ivAvataShop_ScreenHome = findViewById(R.id.ivAvataShop_ScreenHome);

    }

//    Thêm item và ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}