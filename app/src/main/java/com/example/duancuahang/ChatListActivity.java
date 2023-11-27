package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.RecyclerView.ItemPeopleMessage_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    Toolbar toolbar_ChatList;
    RecyclerView rcvChatList;
    ShopData shopData = new ShopData();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ArrayList<String> arrIsCustomerMessage = new ArrayList<>();
    ItemPeopleMessage_Adapter itemPeopleMessageAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setControl();

        context = this;
        setSupportActionBar(toolbar_ChatList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (sharedPreferences.contains("informationShop")) {
            String jsonShop = sharedPreferences.getString("informationShop", "");
            Gson gson = new Gson();
            shopData = gson.fromJson(jsonShop, ShopData.class);
        } else {
            // Dữ liệu không tồn tại, có thể là người dùng đã đăng xuất hoặc lần đầu sử dụng ứng dụng
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        itemPeopleMessageAdapter = new ItemPeopleMessage_Adapter(arrIsCustomerMessage,context);
        rcvChatList.setLayoutManager(new LinearLayoutManager(context));
        rcvChatList.setAdapter(itemPeopleMessageAdapter);
        getChatList();
    }

    private void getChatList() {
        databaseReference = firebaseDatabase.getReference("Message/"+shopData.getIdShop());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrIsCustomerMessage.clear();
                if (snapshot.exists()){
                    for (DataSnapshot itemUser:
                            snapshot.getChildren()) {
                        arrIsCustomerMessage.add(itemUser.getKey());
                        itemPeopleMessageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        toolbar_ChatList = findViewById(R.id.toolbar_ChatList);
        rcvChatList = findViewById(R.id.rcvChatList);
    }
}