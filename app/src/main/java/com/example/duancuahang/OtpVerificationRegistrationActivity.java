package com.example.duancuahang;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class OtpVerificationRegistrationActivity extends AppCompatActivity {

    EditText edtInputOTP;
    Button btnLayMaOTP, btnXacMinhOTP;
    TextView tvTimeResend;
    ProgressBar progressInputOtp;
    Uri uriImageSelectionOnDeviceCCCDFront = null;
    Uri uriImageSelectionOnDeviceCCCDBack = null;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long timeoutSeconds = 60L;
    String verificationCode = "";
    ShopData shopData = new ShopData();
    PhoneAuthProvider.ForceResendingToken resendingToken;
    // Biến kiểm tra upload hình ảnh
    private static boolean bUpload = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverificationregistration);
        context = this;
        //Nhận dữ liệu từ RegistrationActivity
        Intent intent = getIntent();
        shopData = (ShopData) intent.getSerializableExtra("informationShop");
        setControl();
        setEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setEvent() {

        btnXacMinhOTP.setOnClickListener(v -> {
            String enteredOTP = edtInputOTP.getText().toString();
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, enteredOTP);
            registrationSuccessful(phoneAuthCredential);
        });
        btnLayMaOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentOTP(shopData.getShopPhoneNumber(), false);
            }
        });
        edtInputOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validOTP(edtInputOTP.getText().toString()) == false) {
                        edtInputOTP.selectAll();
                        edtInputOTP.requestFocus();
                        edtInputOTP.setError("OTP là dãy ký tự 6 số");
                    }
                }
                return false;
            }
        });
    }


    //Upload thông tin đăng ký tài khoản
    void uploadInfoShop() {
        //uploadCCCDFront(shopData.getShopPhoneNumber());
        //setInProgress(true);
        //RegistrationActivity.uploadCCCDFront(shopPhoneNumber);
//        shopData = new ShopData(status, shopPhoneNumber, shopPhoneNumber, password, shopOwner, shopName, shopAddress,
//                                shopEmail, taxCode, urlImgCCCDFront, urlImgCCCDBack, urlImgShopAvatar);
//        String shopKeyItem = shopData.getShopPhoneNumber();
//
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference shopReference = firebaseDatabase.getReference("Shop");
//        // Upload dữ liệu text
//        shopReference.child(shopKeyItem).setValue(shopData);
    }

    void registration() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shopReference = firebaseDatabase.getReference("Shop");
        // Upload dữ liệu text
        shopReference.child(shopData.getShopPhoneNumber()).setValue(shopData);
    }

    //Upload hình ảnh
//    void registration(String urlImgCCCDFront, String urlImgCCCDBack){
//        shopData = new ShopData(status, shopPhoneNumber, shopPhoneNumber, password, shopOwner, shopName, shopAddress,
//                shopEmail, taxCode, urlImgCCCDFront, urlImgCCCDBack, urlImgShopAvatar);
//        String shopKeyItem = shopData.getShopPhoneNumber();
//
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference shopReference = firebaseDatabase.getReference("Shop");
//        // Upload dữ liệu text
//        shopReference.child(shopKeyItem).setValue(shopData);
//    }

//    void uploadCCCDFront(String shopPhoneNumber){
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

    //    void uploadCCCDBack(String urlCCCDFrontDownloaded, String shopPhoneNumber){
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
//                ShowMessage.showMessage("Lỗi khi tải ảnh lên: "+ e);
//            });
//        });
//    }
    void registrationSuccessful(PhoneAuthCredential phoneAuthCredential) {
//        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    //Nếu nhập đúng mã

                    //Upload dữ liệu đăng ký của người dùng
//                    uploadInfoShop();
                    registration();
                    //Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(OtpVerificationRegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(OtpVerificationRegistrationActivity.this, "Xác minh tài khoản thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    //Nếu nhập sai mã
                    Toast.makeText(OtpVerificationRegistrationActivity.this, "Xác minh OTP thất bại. Vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sentOTP(String shopPhoneNumber, boolean isResend) {
//        setInProgress(true);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+shopData.getShopPhoneNumber().substring(1))
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        verificationCode = phoneAuthCredential.getSmsCode();
                        System.out.println("otp: " + phoneAuthCredential.getSmsCode());
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        System.out.println("loi: " + e.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Xảy ra lỗi trong quá trình gửi mã otp");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        System.out.println("s: "+ s);
                    }

                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressInputOtp.setVisibility(View.VISIBLE);
            btnXacMinhOTP.setVisibility(View.GONE);
        } else {
            progressInputOtp.setVisibility(View.GONE);
            btnXacMinhOTP.setVisibility(View.VISIBLE);
        }
    }

    private void setControl() {
        edtInputOTP = findViewById(R.id.edtInputOTP);
        btnLayMaOTP = findViewById(R.id.btnLayMaOTP);
        btnXacMinhOTP = findViewById(R.id.btnXacMinhOTP);
        tvTimeResend = findViewById(R.id.tvTimeResend);
        progressInputOtp = findViewById(R.id.progressInputOtp);
    }
}