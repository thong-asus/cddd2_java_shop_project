package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.MessageData;
import com.example.duancuahang.RecyclerView.Message_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    Toolbar toolBar_Message;
    RecyclerView rcvContent_Message;
    EditText edtInputContent_Message;
    Button btnSend_Message;
    Context context;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<MessageData> arrMessageData = new ArrayList<>();
    Message_Adapter messageAdapter;
    private Customer customer = new Customer();
    private String idUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context = this;
        customer = new Customer("0123456789", "demo address", "Demo", null);
        setControl();
        setIntiazation();
        getMessageUser();
        setEvent();
    }

    private void sendMessage() {
        databaseReference = firebaseDatabase.getReference("MessageActivity/" + customer.getId() + "/" + idUser);
        MessageData messageData = new MessageData(customer.getId() + "-Customer", edtInputContent_Message.getText().toString(), "", false);
        databaseReference.push().setValue(messageData);
    }

    private void getMessageUser() {
        databaseReference = firebaseDatabase.getReference("MessageActivity/" + customer.getId() + "/" + idUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrMessageData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot itemMessage :
                            snapshot.getChildren()) {
                        MessageData messageData = itemMessage.getValue(MessageData.class);
                        arrMessageData.add(messageData);
                    }
                    messageAdapter.notifyDataSetChanged();
                    rcvContent_Message.scrollToPosition(messageAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        btnSend_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtInputContent_Message.getText().toString().isEmpty()) {
                    sendMessage();
                    clearInput();
                    hideKeyboard();
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
    }
}