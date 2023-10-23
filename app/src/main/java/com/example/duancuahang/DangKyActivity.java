package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duancuahang.Class.Validates;

public class DangKyActivity extends AppCompatActivity {
    EditText edtSoDienThoai, edtTenNguoiDKBanHang,edtTenCuaHang,edtDiaChiCuaHang,edtEmailCuaHang,edtMaSoThue;
    ImageView imgCCCDFront, imgCCCDBack;
    CheckBox chkDongYDieuKhoan;
    Button btnTiepTuc;
    TextView tvDaCoTaiKhoan;
    private Context context;
    private static final int PICK_IMAGE_FRONT = 2;
    private static final int PICK_IMAGE_BACK = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        setControl();
        setEvent();
        context = this;
    }

    private void setEvent() {
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Nếu tất cả các thông tin nhập đầy đủ và checkbox sẽ chuyển sang màn hình nhập otp xác minh tài khoản
                Intent intent = new Intent(context, InputOTPSignUpActivity.class);
                context.startActivity(intent);
            }
        });
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
                        edtTenCuaHang.setError("Tên cửa hàng từ 8-100 ký tự chữ và số");
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
                        edtDiaChiCuaHang.setError("Địa chỉ của cửa hàng từ 8-300 ký tự chữ và số");
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
                        edtEmailCuaHang.setError("1. Email phải có định dạng xxxxx@gmail.com \n 2. Email không được có 2 dấu chấm liền nhau");
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



        //Chọn hình CCCD Mặt trước
        imgCCCDFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(PICK_IMAGE_FRONT);
            }
        });

        //Chọn hình CCCD Mặt sau
        imgCCCDBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(PICK_IMAGE_BACK);
            }
        });


        tvDaCoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(context, DangNhapActivity.class);
                context.startActivity(intent);
            }
        });
    }


    //Hàm chọn ảnh từ thư viện
    private void pickImageFromGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_FRONT) {
                // Xử lý khi chọn ảnh cho mặt trước
                Uri imageUri = data.getData();
                imgCCCDFront.setImageURI(imageUri);
            } else if (requestCode == PICK_IMAGE_BACK) {
                // Xử lý khi chọn ảnh cho mặt sau
                Uri imageUri = data.getData();
                imgCCCDBack.setImageURI(imageUri);
            }
        }
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
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvDaCoTaiKhoan = findViewById(R.id.tvDaCoTaiKhoan);
    }
}