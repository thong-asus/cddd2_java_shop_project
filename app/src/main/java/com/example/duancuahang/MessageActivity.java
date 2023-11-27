package com.example.duancuahang;

import static android.view.View.GONE;

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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.ItemMessage;
import com.example.duancuahang.Class.MessageData;
import com.example.duancuahang.Class.Shop;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Model.NotificationType;
import com.example.duancuahang.Model.SendNotification;
import com.example.duancuahang.RecyclerView.Message_Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    Toolbar toolBar_Message;
    RecyclerView rcvContent_Message;
    EditText edtInputContent_Message;
    Button btnSend_Message;
    Context context;

//    TextView tvCustomerNameChat;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
//    ArrayList<MessageData> arrMessageData = new ArrayList<>();
    Message_Adapter messageAdapter;
    private ShopData shopData = new ShopData();
    private String idUser = "";
    ArrayList<String> arrMessageData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context = this;
        SendNotification.setContext(context);
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
        setControl();
        setIntiazation();
        getMessageUser();
        setEvent();
    }

    private void getCustomerName(){

    }
    private void sendMessage() {
        databaseReference = firebaseDatabase.getReference("ItemMessage");
        String keyPush = databaseReference.push().toString().substring(databaseReference.push().toString().lastIndexOf("/"));
        ItemMessage itemMessage = new ItemMessage(keyPush,shopData.getIdShop()+"-Shop",edtInputContent_Message.getText().toString());
        databaseReference.child(keyPush).setValue(itemMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String content = edtInputContent_Message.getText().toString();
                clearInput();
                hideKeyboard();
                databaseReference = firebaseDatabase.getReference("Message/"+shopData.getIdShop()+"/"+idUser);
                databaseReference.child(itemMessage.getIdItemMessage()+"/idItemMessage").setValue(itemMessage.getIdItemMessage());
                databaseReference = firebaseDatabase.getReference("Message/"+idUser+"/"+shopData.getIdShop());
                databaseReference.child(itemMessage.getIdItemMessage()+"/idItemMessage").setValue(itemMessage.getIdItemMessage());
                sendMessageToShopFcmToken(content);
            }
        });
    }
    private void sendMessageToShopFcmToken(String content){
        databaseReference = firebaseDatabase.getReference("Customer/" + idUser);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //ShopData shopData = snapshot.getValue(ShopData.class);
                    Customer customer = snapshot.getValue(Customer.class);
                    SendNotification.getSendNotificationOrderSuccessFull("fVD1bQEhYJMhezFexMixah:APA91bEkREgHCL7-aDueFGIbdaSuB63zVQIQgulNw3yygwHR_Km--i_hl8Y7nZBcfiIuVUouAP5IDsZbFzRrDExkB9GHAC1xxEigBN7f1wYu6uRVQPv4Umaih9CzO2s8lKdg5NFIbBKC","Cửa hàng: "+shopData.getShopName(),content, NotificationType.NotificationChat());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getMessageUser() {
        databaseReference = firebaseDatabase.getReference("Message/" + shopData.getIdShop()+"/"+idUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrMessageData.clear();
                if(snapshot.exists()){
                    for (DataSnapshot itemMess:
                            snapshot.getChildren()) {
                        String idMessage = itemMess.child("idItemMessage").getValue().toString();
                        arrMessageData.add(idMessage);
                        messageAdapter.notifyDataSetChanged();
                    }
                    rcvContent_Message.smoothScrollToPosition(messageAdapter.getItemCount() -1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
//        if(edtInputContent_Message.getText().toString().isEmpty()){
//            btnSend_Message.setVisibility(GONE);
//        } else {
//            btnSend_Message.setVisibility(View.VISIBLE);
//        }
        btnSend_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtInputContent_Message.getText().toString().isEmpty()) {
                    sendMessage();
                }
            }
        });
    }

    //    xóa giá trị vừa nhập
    private void clearInput() {
        edtInputContent_Message.setText(null);
    }

    //    ẩn bàn phím
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setIntiazation() {
        Intent intent = getIntent();
        idUser = intent.getStringExtra("idUser");

//        gán giá trị cho adapter RecyclerView message
        messageAdapter = new Message_Adapter(arrMessageData, context);
        rcvContent_Message.setLayoutManager(new LinearLayoutManager(context));
        rcvContent_Message.setAdapter(messageAdapter);

        setSupportActionBar(toolBar_Message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        toolBar_Message = findViewById(R.id.toolBar_Message);
        rcvContent_Message = findViewById(R.id.rcvContent_Message);
        edtInputContent_Message = findViewById(R.id.edtInputContent_Message);
        btnSend_Message = findViewById(R.id.btnSend_Message);
//        tvCustomerNameChat = findViewById(R.id.tvCustomerNameChat);
    }
}