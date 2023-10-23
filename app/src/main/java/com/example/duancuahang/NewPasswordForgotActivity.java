package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.duancuahang.Class.Validates;
import com.google.android.material.textfield.TextInputEditText;

public class NewPasswordForgotActivity extends AppCompatActivity {

    TextInputEditText edtMatKhauMoi, edtNhapLaiMatKhauMoi;
    Button btnHoanThanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpasswordforgot);
        setControl();
        setEvent();
    }

    private void setEvent() {
        edtMatKhauMoi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(Validates.validPassword(edtMatKhauMoi.getText().toString()) == false) {
                        edtMatKhauMoi.selectAll();
                        edtMatKhauMoi.requestFocus();
                        edtMatKhauMoi.setError("Mật khẩu tối thiểu 8 ký tự, tối đa 40 ký tự. Bao gồm tối thiểu 1 chữ HOA, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt");
                    }
                }
                return false;
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
                    }
                }
                return false;
            }
        });

        edtNhapLaiMatKhauMoi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(!Validates.validPassword(edtMatKhauMoi.getText().toString()) || !edtMatKhauMoi.getText().toString().equals(edtNhapLaiMatKhauMoi.getText().toString())) {
                        edtNhapLaiMatKhauMoi.selectAll();
                        edtNhapLaiMatKhauMoi.requestFocus();
                        edtNhapLaiMatKhauMoi.setError("Nhập lại mật khẩu không trùng!!!");
                    }
                }
                return false;
            }
        });
    }

    private void setControl() {
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtNhapLaiMatKhauMoi = findViewById(R.id.edtNhapLaiMatKhauMoi);
        btnHoanThanh = findViewById(R.id.btnHoanThanh);
    }
}