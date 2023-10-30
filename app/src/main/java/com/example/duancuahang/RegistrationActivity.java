package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity {
    static EditText edtSoDienThoai, edtTenNguoiDKBanHang,edtTenCuaHang,edtDiaChiCuaHang,edtEmailCuaHang,edtMaSoThue;
    static ImageView imgCCCDFront, imgCCCDBack;
    static CheckBox chkDongYDieuKhoan;
    Button btnTiepTuc;
    TextView tvDaCoTaiKhoan;
    View vRegistrationScreen;
    private Context context;
    // Tạo biến chọn hình ảnh
    private static final int PICK_IMAGE_FRONT = 2;
    private static final int PICK_IMAGE_BACK = 3;
    private static final int READ_EXTERNAL_STORAGE = 123;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    // Tạo biến lưu link hình ảnh trên firebase
    public static String urlImgCCCDFront, urlImgCCCDBack;

    // Uri của mặt trước, mặt sau
    static Uri uriImageSelectionOnDeviceCCCDFront = null;
    static Uri uriImageSelectionOnDeviceCCCDBack = null;

    //private static DatabaseReference databaseReference;
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
                if(checkInfoRegistration()){
                    Intent intent = new Intent(context, OtpVerificationRegistrationActivity.class);
                    //Khởi tạo ShopData
                    ShopData shopData = new ShopData(0,edtSoDienThoai.getText().toString(),edtSoDienThoai.getText().toString(),"",edtTenNguoiDKBanHang.getText().toString(),
                            edtTenCuaHang.getText().toString(),edtDiaChiCuaHang.getText().toString(),edtEmailCuaHang.getText().toString(),
                            edtMaSoThue.getText().toString(),urlImgCCCDFront,urlImgCCCDBack,"");
                    // Đính kèm đối tượng ShopData vào Intent
                    intent.putExtra("informationShop", shopData);
                    startActivity(intent);
                    finish();
                }
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
                    } else {edtTenNguoiDKBanHang.requestFocus();}
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
                    } else {edtTenCuaHang.requestFocus();}
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
                    } else {edtDiaChiCuaHang.requestFocus();}
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
                        edtDiaChiCuaHang.setError("1. Địa chỉ của cửa hàng từ 8-300 ký tự chữ và số.\n2. Không được phép 2 dấu / hoặc . liền kề nhau.");
                    } else {edtEmailCuaHang.requestFocus();}
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
                    } else {edtMaSoThue.requestFocus();}
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
                    } else {hideKeyboard();}
                }
                return false;
            }
        });



        //Chọn hình CCCD Mặt trước
        imgCCCDFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickImageFromGallery(PICK_IMAGE_FRONT);
                if (ContextCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Kiểm tra xem đã cấp quyền truy cập thư viện ảnh chưa? Nếu chưa thì yêu cầu?
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    pickImageFromGallery(PICK_IMAGE_FRONT);
                    // Quyền đã được cấp, thực hiện hành động cần thiết.
                }
            }
        });

        //Chọn hình CCCD Mặt sau
        imgCCCDBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Kiểm tra xem đã cấp quyền truy cập thư viện ảnh chưa? Nếu chưa thì yêu cầu?
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // Nếu đã cấp quyền code tại đây
                    pickImageFromGallery(PICK_IMAGE_BACK);
                }
            }
        });


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

//    private static void registration(String urlImgCCCDFront, String urlImgCCCDBack){
//        String keyShopItem = edtSoDienThoai.getText().toString();
//        ShopData shopData = new ShopData(0,keyShopItem,edtSoDienThoai.getText().toString(),"", edtTenNguoiDKBanHang.getText().toString(),
//                edtTenCuaHang.getText().toString(), edtDiaChiCuaHang.getText().toString(),edtEmailCuaHang.getText().toString(),
//                edtMaSoThue.getText().toString(), urlImgCCCDFront,urlImgCCCDBack,"");
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference();
//        databaseReference.child("Shop").child(keyShopItem).setValue(shopData);
//        //ShowMessage.showMessage("Đăng ký thành công");
//    }

    public static boolean checkInfoRegistration() {
        if (edtSoDienThoai.getText().toString().isEmpty() || edtTenNguoiDKBanHang.getText().toString().isEmpty() || edtTenCuaHang.getText().toString().isEmpty() ||
                edtDiaChiCuaHang.getText().toString().isEmpty() || edtEmailCuaHang.getText().toString().isEmpty() || edtMaSoThue.getText().toString().isEmpty() ||
                uriImageSelectionOnDeviceCCCDFront == null || uriImageSelectionOnDeviceCCCDBack == null || !Validates.validPhone(edtSoDienThoai.getText().toString()) ||
                !Validates.validFullname(edtTenNguoiDKBanHang.getText().toString()) || !Validates.validShopName(edtTenCuaHang.getText().toString()) ||
                !Validates.validShopAddress(edtDiaChiCuaHang.getText().toString()) || !Validates.validEmail(edtEmailCuaHang.getText().toString()) ||
                !Validates.validMaSoThue(edtMaSoThue.getText().toString())) {
            ShowMessage.showMessage("Không được bỏ trống bất kỳ thông tin đăng ký HOẶC Thông tin bạn nhập không đúng định dạng. Vui lòng thử lại!!!");
            return false;
        }
        if (!chkDongYDieuKhoan.isChecked()) {
            ShowMessage.showMessage("Bạn cần phải đồng ý với điều khoản dịch vụ của chúng tôi!");
            return false;
        }
        return true;
    }

    //Hàm tải ảnh CCCD lên FireBase
//    public static void uploadCCCDFront(String shopPhoneNumber){
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        String[] part = uriImageSelectionOnDeviceCCCDFront.getLastPathSegment().split("/");
//        StorageReference imgRef = storageRef.child("imageShop/" + shopPhoneNumber + "/" + (part[part.length-1]));
//        UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDFront);
//        uploadTask.addOnCompleteListener(taskSnapshot -> {
//            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                urlImgCCCDFront = uri.toString();
//                bUpload = true;
//                if (bUpload){
//                    uploadCCCDBack(urlImgCCCDFront,shopPhoneNumber);
//                }
//            }).addOnFailureListener(e -> {
//                ShowMessage.showMessage("Lỗi khi tải ảnh lên: "+ e);
//            });
//        });
//    }
//
//    public static void uploadCCCDBack(String urlCCCDFrontDownloaded, String shopPhoneNumber){
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        String[] part = uriImageSelectionOnDeviceCCCDBack.getLastPathSegment().split("/");
//        StorageReference imgRef = storageRef.child("imageShop/" + shopPhoneNumber + "/" + (part[part.length-1]));
//        UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDBack);
//        uploadTask.addOnCompleteListener(taskSnapshot -> {
//            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                urlImgCCCDBack = uri.toString();
//                bUpload = true;
//                if (bUpload){
//                    registration(urlCCCDFrontDownloaded,urlImgCCCDBack);
//                }
//            }).addOnFailureListener(e -> {
//                    ShowMessage.showMessage("Lỗi khi tải ảnh lên: "+ e);
//            });
//        });
//    }


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
                uriImageSelectionOnDeviceCCCDFront = data.getData();
                imgCCCDFront.setImageURI(uriImageSelectionOnDeviceCCCDFront);
            } else if (requestCode == PICK_IMAGE_BACK) {
                // Xử lý khi chọn ảnh cho mặt sau
                uriImageSelectionOnDeviceCCCDBack = data.getData();
                imgCCCDBack.setImageURI(uriImageSelectionOnDeviceCCCDBack);
            }
        }
    }
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
        imgCCCDFront = findViewById(R.id.imgCCCDFront);
        imgCCCDBack = findViewById(R.id.imgCCCDBack);
        chkDongYDieuKhoan = findViewById(R.id.chkDongYDieuKhoan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvDaCoTaiKhoan = findViewById(R.id.tvDaCoTaiKhoan);
    }
}