package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.Shop;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.example.duancuahang.FireBaseAuthenticator.FireBaseAuthenticator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //Biến giao diện
    EditText edtSoDienThoai,edtMatKhau;
    CheckBox chkLuuTaiKhoan;
    Button btnDangNhap;
    TextView tvQuenMatKhau, tvDangKyTaiKhoan;
    View vLoginScreen;
    private Context context;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
        context = this;
        ShowMessage.context = this;
    }

    private void setEvent() {
        //Xử lý sự kiện click nút Đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(edtSoDienThoai.getText().toString(), edtMatKhau.getText().toString(), context);
            }
        });

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ForgotPasswordActivity.class);
                context.startActivity(intent);
            }
        });
        //Chuyển sang màn hình đăng ký
        tvDangKyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
            }
        });
        edtSoDienThoai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validPhone(edtSoDienThoai.getText().toString()) == false) {
                        edtSoDienThoai.selectAll();
                        edtSoDienThoai.requestFocus();
                        edtSoDienThoai.setError("Số điện thoại phải đủ 10 ký tự số và bắt đầu bằng số 0!");
                    }
                }
                return false;
            }
        });
        edtMatKhau.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validPassword(edtMatKhau.getText().toString()) == false) {
                        edtMatKhau.selectAll();
                        edtMatKhau.requestFocus();
                        edtMatKhau.setError("Mật khẩu không được bỏ trống!");
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

        //Sự kiện ẩn bàn phím
        vLoginScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
    }

    public static void login(final String shopPhoneNumber, final String password, final Context context) {
        // Kiểm tra số điện thoại hoặc mật khẩu có bị bỏ trống hay không
        if (shopPhoneNumber.isEmpty() || password.isEmpty()) {
            ShowMessage.showMessage("Thông tin đăng nhập không được bỏ trống!!!");
            return;
        }

        final DatabaseReference accountRef;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        accountRef = firebaseDatabase.getReference("Shop");
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Biến xác định tài khoản có tồn tại hay không?
                boolean found = false;

                if (snapshot.exists()) {
                    for (DataSnapshot accountShopSnapshot : snapshot.getChildren()) {
                        Shop shopAccount = accountShopSnapshot.getValue(Shop.class);
                        if (shopAccount != null && shopAccount.getShopPhoneNumber().equals(shopPhoneNumber)) {

                            //Tạo biến kiểm tra
                            int storedStatus = shopAccount.getStatus();
                            String storedPassword = shopAccount.getPassword();

                            // Kiểm tra trạng thái tài khoản
                            if (storedStatus == 0 && storedPassword.equals(password)) {
                                return;
                            } else if (storedStatus == 2 && storedPassword.equals(password)) {
                                ShowMessage.showMessage("Tài khoản đã bị khóa.\nLiên hệ ADMIN để biết thêm chi tiết!");
                                return;
                            } else {
                                // Kiểm tra mật khẩu
                                if (storedPassword.equals(password)) {
                                    // Đăng nhập thành công, chuyển sang màn hình HomeShop
                                    Intent intent  = new Intent(context, HomeShop.class);
                                    context.startActivity(intent);
                                    return;
                                } else {
                                    ShowMessage.showMessage("Sai mật khẩu. Vui lòng thử lại!!!");
                                }
                            }
                            found = true;
                        }
                    }
                    // Không tìm thấy tài khoản
                    if(!found){
                        ShowMessage.showMessage("Tài khoản không tồn tại!!!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ShowMessage.showMessage("Lỗi không truy vấn được dữ liệu");
            }
        });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void setControl() {
        vLoginScreen = findViewById(R.id.vLoginScreen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuTaiKhoan = findViewById(R.id.chkLuuTaiKhoan);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        tvDangKyTaiKhoan = findViewById(R.id.tvDangKyTaiKhoan);
    }
}