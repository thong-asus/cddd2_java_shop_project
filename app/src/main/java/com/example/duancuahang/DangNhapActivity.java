package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.Validates;

public class DangNhapActivity extends AppCompatActivity {

    EditText edtSoDienThoai,edtMatKhau;
    CheckBox chkLuuTaiKhoan;
    Button btnDangNhap;
    TextView tvQuenMatKhau, tvDangKyTaiKhoan;
    private Context context;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
        context = this;
    }

    private void setEvent() {
        //Xử lý sự kiện click nút Đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,QuenMatKhauActivity.class);
                context.startActivity(intent);
            }
        });
        //Chuyển sang màn hình đăng ký
        tvDangKyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DangKyActivity.class);
                context.startActivity(intent);
            }
        });
        edtSoDienThoai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validPhone(edtSoDienThoai.getText().toString())==false){
                        edtSoDienThoai.selectAll();
                        edtSoDienThoai.requestFocus();
                        edtSoDienThoai.setError("Số điện thoại phải đủ 10 ký tự số và bắt đầu bằng số 0");
                    }
                }
                return false;
            }
        });
        edtMatKhau.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validPassword(edtMatKhau.getText().toString())==false){
                        edtMatKhau.selectAll();
                        edtMatKhau.requestFocus();
                        edtMatKhau.setError("Mật khẩu tối thiểu 8 ký tự, tối đa 40 ký tự. Bao gồm tối thiểu 1 chữ HOA, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt");
                    }
                }
                return false;
            }
        });


        //Lưu số điện thoại//////////////////////////////////////////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences("MyPhoneNumber", Context.MODE_PRIVATE);

        boolean isChecked = sharedPreferences.getBoolean("isChecked", false);
        chkLuuTaiKhoan.setChecked(isChecked);
        if (isChecked) {
            String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        }
        chkLuuTaiKhoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //Nếu đã Check
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", isChecked);

                if (isChecked) {
                    // Lưu số điện thoại nếu đã được đăng nhập
                    String phoneNumber = edtSoDienThoai.getText().toString(); // Thay bằng số điện thoại thực tế
                    editor.putString("phoneNumber", phoneNumber);
                } else {
                    // Xoá số điện thoại nếu checkbox được bỏ chọn
                    editor.remove("phoneNumber");
                }
                editor.apply();
            }
        });

    }

    private void setControl() {
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuTaiKhoan = findViewById(R.id.chkLuuTaiKhoan);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        tvDangKyTaiKhoan = findViewById(R.id.tvDangKyTaiKhoan);
    }
}