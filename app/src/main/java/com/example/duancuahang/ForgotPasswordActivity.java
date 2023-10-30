package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.Validates;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtSoDienThoai, edtInputOTP;
    Button btnLayMaOTP, btnTiepTuc;
    TextView tvTimeResend;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        setControl();
        setEvent();
        context = this;
    }

    private void setEvent() {
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewPasswordForgotActivity.class);
                context.startActivity(intent);
            }
        });
        edtSoDienThoai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(Validates.validPhone(edtSoDienThoai.getText().toString()) == false) {
                        edtSoDienThoai.selectAll();
                        edtSoDienThoai.requestFocus();
                        edtSoDienThoai.setError("Số điện thoại phải đủ 10 ký tự số và bắt đầu bằng số 0");
                    }
                }
                return false;
            }
        });
        edtInputOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(Validates.validOTP(edtInputOTP.getText().toString()) == false){
                        edtInputOTP.selectAll();
                        edtInputOTP.requestFocus();
                        edtInputOTP.setError("OTP là dãy ký tự 6 số");
                    }
                }
                return false;
            }
        });
    }

    private void setControl() {
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtInputOTP = findViewById(R.id.edtInputOTP);
        btnLayMaOTP = findViewById(R.id.btnLayMaOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvTimeResend = findViewById(R.id.tvTimeResend);
    }
}