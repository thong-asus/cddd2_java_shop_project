package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DangKyActivity extends AppCompatActivity {
    EditText edtSoDienThoai, edtTenNguoiDKBanHang,edtTenCuaHang,edtDiaChiCuaHang,edtEmailCuaHang,edtMaSoThue;
    ImageView imgCCCDFront, imgCCCDBack;
    CheckBox chkDongYDieuKhoan;
    Button btn_tiepTuc;
    TextView tvDaCoTaiKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        setControl();
        setEvent();
    }

    private void setEvent() {
        edtSoDienThoai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validPhone(edtSoDienThoai.getText().toString()) == false) {
                        edtSoDienThoai.selectAll();
                        edtSoDienThoai.requestFocus();
                        edtSoDienThoai.setError("Số điện thoại phải đủ 10 ký tự số và bắt đầu bằng số 0");
                    }
                }
                return false;
            }
        });
        edtTenNguoiDKBanHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validFullname(edtTenNguoiDKBanHang.getText().toString()) == false) {
                        edtTenNguoiDKBanHang.selectAll();
                        edtTenNguoiDKBanHang.requestFocus();
                        edtTenNguoiDKBanHang.setError("Tên thật của người đăng ký bán hàng từ 8-70 ký tự chữ");
                    }
                }
                return false;
            }
        });
        edtTenCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validShopName(edtTenCuaHang.getText().toString()) == false) {
                        edtTenCuaHang.selectAll();
                        edtTenCuaHang.requestFocus();
                        edtTenCuaHang.setError("Tên cửa hàng từ 8-100 ký tự chữ số");
                    }
                }
                return false;
            }
        });
        edtDiaChiCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validShopAddress(edtDiaChiCuaHang.getText().toString()) == false) {
                        edtDiaChiCuaHang.selectAll();
                        edtDiaChiCuaHang.requestFocus();
                        edtDiaChiCuaHang.setError("Địa chỉ của cửa hàng từ 8-300 ký tự chữ số");
                    }
                }
                return false;
            }
        });
        edtEmailCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validEmail(edtEmailCuaHang.getText().toString()) == false) {
                        edtEmailCuaHang.selectAll();
                        edtEmailCuaHang.requestFocus();
                        edtEmailCuaHang.setError("Email phải có định dạng xxxxx@gmail.com");
                    }
                }
                return false;
            }
        });
        edtMaSoThue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(Validates.validMaSoThue(edtMaSoThue.getText().toString()) == false) {
                        edtMaSoThue.selectAll();
                        edtMaSoThue.requestFocus();
                        edtMaSoThue.setError("Mã số thuế bao gồm 13 ký tự số");
                    }
                }
                return false;
            }
        });


        //Chuyển sang màn hình đăng nhập
        tvDaCoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(DangNhapActivity.class);
                setContentView(R.layout.activity_dang_nhap);
            }
        });
    }

    private void setControl() {
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtTenNguoiDKBanHang = findViewById(R.id.edtTenNguoiDKBanHang);
        edtTenCuaHang = findViewById(R.id.edtTenCuaHang);
        edtDiaChiCuaHang = findViewById(R.id.edtDiaChiCuaHang);
        edtEmailCuaHang = findViewById(R.id.edtEmailCuaHang);
        edtMaSoThue = findViewById(R.id.edtMaSoThue);
        imgCCCDFront = findViewById(R.id.imgCCCDFront);
        imgCCCDBack = findViewById(R.id.imgCCCDBack);
        chkDongYDieuKhoan = findViewById(R.id.chkDongYDieuKhoan);
        btn_tiepTuc = findViewById(R.id.btn_tiepTuc);
        tvDaCoTaiKhoan = findViewById(R.id.tvDaCoTaiKhoan);
    }
}