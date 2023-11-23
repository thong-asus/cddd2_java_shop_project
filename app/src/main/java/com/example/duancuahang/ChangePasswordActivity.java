package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText edtMatKhauMoi, edtXacMinhMatKhauMoi;
    Button btnHoanThanh;
    Toolbar toolbar_ChangePassword;
    private ShopData shopData = new ShopData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        setControl();
        setIntiazation();
        setEvent();
    }

    private void setEvent() {
        btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    updatePassword();
            }
        });
        edtMatKhauMoi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(Validates.validPassword(edtMatKhauMoi.getText().toString()) == false) {
                        edtMatKhauMoi.selectAll();
                        edtMatKhauMoi.requestFocus();
                        edtMatKhauMoi.setError("Mật khẩu tối thiểu 8 ký tự, tối đa 40 ký tự. Bao gồm tối thiểu 1 chữ HOA, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt");
                    } else {
                        edtXacMinhMatKhauMoi.requestFocus();
                    }
                }
                return false;
            }
        });
        edtXacMinhMatKhauMoi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(!Validates.validPassword(edtMatKhauMoi.getText().toString()) || !edtMatKhauMoi.getText().toString().equals(edtXacMinhMatKhauMoi.getText().toString())) {
                        edtXacMinhMatKhauMoi.selectAll();
                        edtXacMinhMatKhauMoi.requestFocus();
                        edtXacMinhMatKhauMoi.setError("Mật khẩu và xác nhận mật khẩu không khớp!");
                    } else {
                        hideKeyboard();
                    }
                }
                return false;
            }
        });
    }

    private void updatePassword(){
        String matKhauMoi = edtMatKhauMoi.getText().toString();
        //Kiểm tra mật khẩu
        if(checkNewPassword()){
            if(checkOldAndNewPassword()){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference shopReference = firebaseDatabase.getReference("Shop");
                if(shopReference != null){
                    shopData.setPassword(matKhauMoi);
                    shopReference.child(shopData.getIdShop()).setValue(shopData);
                    Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, InformationAccountActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this, "Thay đổi mật khẩu thất bại!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkOldAndNewPassword(){
        if(edtMatKhauMoi.getText().toString().equals(shopData.getPassword().toString())){
            Toast.makeText(this, "Mật khẩu mới không được trùng mật khẩu cũ!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean checkNewPassword() {
        String matKhauMoi = edtMatKhauMoi.getText().toString();
        String xacMinhMatKhauMoi = edtXacMinhMatKhauMoi.getText().toString();

        if (matKhauMoi.isEmpty() || xacMinhMatKhauMoi.isEmpty() || !Validates.validPassword(matKhauMoi) || !Validates.validPassword(xacMinhMatKhauMoi)) {
            // Hiển thị thông báo nếu một trong hai trường mật khẩu trống
            Toast.makeText(this, "Mật khẩu không được để trống HOẶC sai yêu cầu độ mạnh!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!matKhauMoi.equals(xacMinhMatKhauMoi) || !Validates.validPassword(matKhauMoi) || !Validates.validPassword(xacMinhMatKhauMoi)) {
            // Hiển thị thông báo nếu mật khẩu và xác nhận mật khẩu không khớp
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp HOẶC sai yêu cầu độ mạnh!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }
    private void setIntiazation() {
        //Kích hoạt back button on toolbar
        setSupportActionBar(toolbar_ChangePassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Event click back  button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }
    private void setControl() {
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacMinhMatKhauMoi = findViewById(R.id.edtXacMinhMatKhauMoi);
        btnHoanThanh = findViewById(R.id.btnHoanThanh);
        toolbar_ChangePassword = findViewById(R.id.toolbar_ChangePassword);
    }
}