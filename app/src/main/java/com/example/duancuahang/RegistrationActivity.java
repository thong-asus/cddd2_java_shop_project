package com.example.duancuahang;

import static android.content.Intent.ACTION_PICK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegistrationActivity extends AppCompatActivity {
    static EditText edtSoDienThoai, edtTenNguoiDKBanHang, edtTenCuaHang, edtDiaChiCuaHang, edtEmailCuaHang, edtMaSoThue;
    static ImageView imgCCCDFront, imgCCCDBack;
    static CheckBox chkDongYDieuKhoan;
    Button btnTiepTuc;
    TextView tvDaCoTaiKhoan;
    View vRegistrationScreen;
    private Context context;
    // Tạo biến chọn hình ảnh
    private static final int PICK_IMAGE_FRONT = 2;
    private static final int PICK_IMAGE_BACK = 3;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_PICK = 1;

    // Tạo biến lưu link hình ảnh trên firebase
    private static String urlImgCCCDFront, urlImgCCCDBack;

    // Uri của mặt trước, mặt sau
    static Uri uriImageSelectionOnDeviceCCCDFront = null;
    static Uri uriImageSelectionOnDeviceCCCDBack = null;
    private int dem = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setControl();
        setEvent();
        context = this;
        ShowMessage.context = this;
    }

    private void setEvent() {
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kiểm tra dữ liệu nhập vào hợp lệ
                if (checkInfoRegistration()) {
                    Intent intent = new Intent(context, OtpVerificationRegistrationActivity.class);
                    //Khởi tạo ShopData
                    ShopData shopData = new ShopData(0, edtSoDienThoai.getText().toString(), edtSoDienThoai.getText().toString(), "", edtTenNguoiDKBanHang.getText().toString(),
                            edtTenCuaHang.getText().toString(), edtDiaChiCuaHang.getText().toString(), edtEmailCuaHang.getText().toString(),
                            edtMaSoThue.getText().toString());
                    // Đính kèm đối tượng ShopData vào Intent
//
                    intent.putExtra("informationShop", shopData);
//                    intent.putExtra("urifront", uriImageSelectionOnDeviceCCCDFront);
//                    intent.putExtra("uriback", uriImageSelectionOnDeviceCCCDBack);

                    //uploadCCCD(edtSoDienThoai.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
        edtSoDienThoai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validPhone(edtSoDienThoai.getText().toString()) == false) {
                        edtSoDienThoai.selectAll();
                        edtSoDienThoai.requestFocus();
                        edtSoDienThoai.setError("Số điện thoại phải đủ 10 ký tự số và bắt đầu bằng số 0");
                    } else {
                        edtTenNguoiDKBanHang.requestFocus();
                    }
                }
                return false;
            }
        });
        edtTenNguoiDKBanHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validFullname(edtTenNguoiDKBanHang.getText().toString()) == false) {
                        edtTenNguoiDKBanHang.selectAll();
                        edtTenNguoiDKBanHang.requestFocus();
                        edtTenNguoiDKBanHang.setError("Tên thật của người đăng ký bán hàng từ 8-70 ký tự chữ");
                    } else {
                        edtTenCuaHang.requestFocus();
                    }
                }
                return false;
            }
        });
        edtTenCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validShopName(edtTenCuaHang.getText().toString()) == false) {
                        edtTenCuaHang.selectAll();
                        edtTenCuaHang.requestFocus();
                        edtTenCuaHang.setError("Tên cửa hàng từ 8-100 ký tự chữ và số");
                    } else {
                        edtDiaChiCuaHang.requestFocus();
                    }
                }
                return false;
            }
        });
        edtDiaChiCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validShopAddress(edtDiaChiCuaHang.getText().toString()) == false) {
                        edtDiaChiCuaHang.selectAll();
                        edtDiaChiCuaHang.requestFocus();
                        edtDiaChiCuaHang.setError("1. Địa chỉ của cửa hàng từ 8-300 ký tự chữ và số.\n2. Không được phép 2 dấu / hoặc . liền kề nhau.");
                    } else {
                        edtEmailCuaHang.requestFocus();
                    }
                }
                return false;
            }
        });
        edtEmailCuaHang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validEmail(edtEmailCuaHang.getText().toString()) == false) {
                        edtEmailCuaHang.selectAll();
                        edtEmailCuaHang.requestFocus();
                        edtEmailCuaHang.setError("1. Email phải có định dạng xxxxx@gmail.com \n 2. Email không được có 2 dấu chấm liền nhau");
                    } else {
                        edtMaSoThue.requestFocus();
                    }
                }
                return false;
            }
        });
        edtMaSoThue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validMaSoThue(edtMaSoThue.getText().toString()) == false) {
                        edtMaSoThue.selectAll();
                        edtMaSoThue.requestFocus();
                        edtMaSoThue.setError("Mã số thuế bao gồm 13 ký tự số");
                    } else {
                        hideKeyboard();
                    }
                }
                return false;
            }
        });


//        imgCCCDFront.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    openGallery(PICK_IMAGE_FRONT);
//                } else {
//                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//            }
//        });
//
//        imgCCCDBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    openGallery(PICK_IMAGE_BACK);
//                } else {
//                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//            }
//        });


//Chọn hình CCCD Mặt trước
//        imgCCCDFront.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dem = 1;
//                pickImageFromGallery(PICK_IMAGE_FRONT);
//            }
//        });
//
////Chọn hình CCCD Mặt sau
//        imgCCCDBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dem = 2;
//                pickImageFromGallery(PICK_IMAGE_BACK);
//            }
//        });

//        imgCCCDFront.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGallery(PICK_IMAGE_FRONT);
//            }
//        });
//        imgCCCDBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGallery(PICK_IMAGE_BACK);
//            }
//        });


        tvDaCoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        vRegistrationScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });

    }
    private boolean isAccountExists(String shopPhoneNumber) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop");

        // Sử dụng CountDownLatch để đồng bộ hóa
        CountDownLatch countDownLatch = new CountDownLatch(1);

        AtomicBoolean exists = new AtomicBoolean(false);

        databaseReference.child(shopPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exists.set(snapshot.exists());
                countDownLatch.countDown(); // Giảm đếm khi dữ liệu đã được xử lý
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                countDownLatch.countDown(); // Giảm đếm nếu có lỗi
            }
        });

        try {
            // Chờ cho đến khi đồng bộ hóa hoàn thành (timeout sau một khoảng thời gian nhất định)
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exists.get();
    }


    public boolean checkInfoRegistration() {
        if (edtSoDienThoai.getText().toString().isEmpty() || edtTenNguoiDKBanHang.getText().toString().isEmpty() || edtTenCuaHang.getText().toString().isEmpty() ||
                edtDiaChiCuaHang.getText().toString().isEmpty() || edtEmailCuaHang.getText().toString().isEmpty() || edtMaSoThue.getText().toString().isEmpty() ||
                !Validates.validPhone(edtSoDienThoai.getText().toString()) ||
                !Validates.validFullname(edtTenNguoiDKBanHang.getText().toString()) || !Validates.validShopName(edtTenCuaHang.getText().toString()) ||
                !Validates.validShopAddress(edtDiaChiCuaHang.getText().toString()) || !Validates.validEmail(edtEmailCuaHang.getText().toString()) ||
                !Validates.validMaSoThue(edtMaSoThue.getText().toString())) {
            ShowMessage.showMessage(RegistrationActivity.this, "Không được bỏ trống bất kỳ thông tin đăng ký HOẶC Thông tin bạn nhập không đúng định dạng. Vui lòng thử lại!!!");
            return false;
        }
        if (!chkDongYDieuKhoan.isChecked()) {
            ShowMessage.showMessage(RegistrationActivity.this, "Bạn cần phải đồng ý với điều khoản dịch vụ của chúng tôi!");
            return false;
        }
        return true;
    }
//    public boolean checkInfoRegistration() {
//        if (edtSoDienThoai.getText().toString().isEmpty() || edtTenNguoiDKBanHang.getText().toString().isEmpty() || edtTenCuaHang.getText().toString().isEmpty() ||
//                edtDiaChiCuaHang.getText().toString().isEmpty() || edtEmailCuaHang.getText().toString().isEmpty() || edtMaSoThue.getText().toString().isEmpty() ||
//                uriImageSelectionOnDeviceCCCDFront == null || uriImageSelectionOnDeviceCCCDBack == null || !Validates.validPhone(edtSoDienThoai.getText().toString()) ||
//                !Validates.validFullname(edtTenNguoiDKBanHang.getText().toString()) || !Validates.validShopName(edtTenCuaHang.getText().toString()) ||
//                !Validates.validShopAddress(edtDiaChiCuaHang.getText().toString()) || !Validates.validEmail(edtEmailCuaHang.getText().toString()) ||
//                !Validates.validMaSoThue(edtMaSoThue.getText().toString())) {
//            ShowMessage.showMessage(RegistrationActivity.this,"Không được bỏ trống bất kỳ thông tin đăng ký HOẶC Thông tin bạn nhập không đúng định dạng. Vui lòng thử lại!!!");
//            return false;
//        }
//        if (!chkDongYDieuKhoan.isChecked()) {
//            ShowMessage.showMessage(RegistrationActivity.this,"Bạn cần phải đồng ý với điều khoản dịch vụ của chúng tôi!");
//            return false;
//        }
//        return true;
//    }

    void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }
    //Hàm chọn ảnh từ thư viện
//    private void pickImageFromGallery(int requestCode) {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, requestCode);
//    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data!=null) {
//            if (requestCode == PICK_IMAGE_FRONT && data!=null) {
//                // Xử lý khi chọn ảnh cho mặt trước
//                uriImageSelectionOnDeviceCCCDFront = data.getData();
//                imgCCCDFront.setImageURI(uriImageSelectionOnDeviceCCCDFront);
//                System.out.println("uri front: " + uriImageSelectionOnDeviceCCCDFront);
//            } else if (requestCode == PICK_IMAGE_BACK && data!=null) {
//                // Xử lý khi chọn ảnh cho mặt sau
//                uriImageSelectionOnDeviceCCCDBack = data.getData();
//                urlImgCCCDBack = getContentResolver().getType(uriImageSelectionOnDeviceCCCDBack);
//                imgCCCDBack.setImageURI(uriImageSelectionOnDeviceCCCDBack);
//                System.out.println("backkkkkkk" + urlImgCCCDBack);
//
//            }
//        }
//}
    //Sự kiện ẩn bàn phím
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setControl() {
        vRegistrationScreen = findViewById(R.id.vRegistrationScreen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtTenNguoiDKBanHang = findViewById(R.id.edtTenNguoiDKBanHang);
        edtTenCuaHang = findViewById(R.id.edtTenCuaHang);
        edtDiaChiCuaHang = findViewById(R.id.edtDiaChiCuaHang);
        edtEmailCuaHang = findViewById(R.id.edtEmailCuaHang);
        edtMaSoThue = findViewById(R.id.edtMaSoThue);
//        imgCCCDFront = findViewById(R.id.imgCCCDFront);
//        imgCCCDBack = findViewById(R.id.imgCCCDBack);
        chkDongYDieuKhoan = findViewById(R.id.chkDongYDieuKhoan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvDaCoTaiKhoan = findViewById(R.id.tvDaCoTaiKhoan);
    }
}