package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HomeShop extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linearLayout_SanPhamCuaToi_ScreenHome;
    TextView tvNameShop_ScreenHome;
    ImageView ivAvataShop_ScreenHome;
    Context context;
    CallbackManager callbackManager;
    private ShopData shopData = new ShopData();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shop);
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop",Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        callbackManager = CallbackManager.Factory.create();
        context = this;
        setControl();
        setIntiazation();
        setEvent();
        setSupportActionBar(toolbar);
    }

    private void setIntiazation() {
        tvNameShop_ScreenHome.setText(shopData.getShopName());
        if (shopData.getUrlImgShopAvatar().isEmpty()){
            ivAvataShop_ScreenHome.setImageResource(R.drawable.iconshop);
        }
        else {
            Picasso.get().load(shopData.getUrlImgShopAvatar()).into(ivAvataShop_ScreenHome);
        }

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
                Intent intent = new Intent(context,Productlist.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

//    Anh xa
    private void setControl() {
        toolbar = findViewById(R.id.toolBar_ScreenHome);
        linearLayout_SanPhamCuaToi_ScreenHome = findViewById(R.id.linearLayout_SanPhamCuaToi_ScreenHome);
        tvNameShop_ScreenHome = findViewById(R.id.tvNameShop_ScreenHome);
        ivAvataShop_ScreenHome = findViewById(R.id.ivAvataShop_ScreenHome);
    }

//    Thêm item và ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}