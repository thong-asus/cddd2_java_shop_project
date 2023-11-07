package com.example.duancuahang;


import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.Shop;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Validates;
import com.google.android.gms.common.GooglePlayServicesIncorrectManifestValueException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpVerificationRegistrationActivity extends AppCompatActivity {

    EditText edtInputOTP;
    Button btnLayMaOTP, btnXacMinhOTP;
    TextView tvTimeResend;
    ProgressBar progressInputOtp;
   private Uri uriImageSelectionOnDeviceCCCDFront = null;
   private Uri uriImageSelectionOnDeviceCCCDBack = null;
    View vOtpVerificationRegistration;

    //////////////////////////////////BIẾN XỬ LÝ
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long timeoutSeconds = 60L;
    String verificationCode = "", codesms = "";
    int status;
    private ShopData shopData = new ShopData();
    String shopPhoneNumber;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    Context context;
    boolean bPushImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverificationregistration);
        context = this;
        //Nhận dữ liệu từ RegistrationActivity
        Intent intent = getIntent();
        shopData = (ShopData) intent.getSerializableExtra("informationShop");
        uriImageSelectionOnDeviceCCCDFront = (Uri)intent.getParcelableExtra("urifront");
        uriImageSelectionOnDeviceCCCDBack = (Uri)intent.getParcelableExtra("uriback");

        System.out.println("front" + uriImageSelectionOnDeviceCCCDFront);
        System.out.println("back" + uriImageSelectionOnDeviceCCCDBack);
        ////////////////////////////////////////////////////////////////////////////////////////////
        setControl();
        setEvent();

    }

    private void setEvent() {
//        btnXacMinhOTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //uploadCCCDFront();
//                registration();
//                //uploadCCCDBack();
//            }
//        });
        btnXacMinhOTP.setOnClickListener(v -> {
            if(edtInputOTP.getText().toString().isEmpty() || !Validates.validOTP(edtInputOTP.getText().toString())){
                Toast.makeText(OtpVerificationRegistrationActivity.this, "OTP không được bỏ trống HOẶC OTP sai định dạng. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            } else {
                String enteredOTP = edtInputOTP.getText().toString();
                PhoneAuthCredential phoneAuthCredential = null;
                try {
                    phoneAuthCredential = PhoneAuthProvider.getCredential(codesms, enteredOTP);
                }
                catch (Exception e){
                    Toast.makeText(context, "Lỗi xác minh OTP" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(phoneAuthCredential != null){
                    registrationSuccessful(phoneAuthCredential);
                }
            }
    });
        btnLayMaOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentOTP();
                resendOTP();
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
        vOtpVerificationRegistration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
    }


    void registration(){
        shopData.setUrlImgShopAvatar("");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shopReference = firebaseDatabase.getReference("Shop");
        // Upload dữ liệu text
        shopReference.child(shopData.getShopPhoneNumber()).setValue(shopData);
    }

    //private void uploadCCCDBack(){
        // Get the data from an ImageView as bytes


    private void pushCCCDBack(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String[] part = uriImageSelectionOnDeviceCCCDBack.getLastPathSegment().split("/");
        StorageReference imgRef = storageRef.child("imageShop/" + shopData.getShopPhoneNumber() + "/" + (part[part.length-1]));

//        try {
            UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDBack);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        shopData.setUrlImgCCCDFront(uri.toString());
                        if (!bPushImage){
                            registration();
                            bPushImage = true;
                        }
                    });
                }
            });
//        } catch (Exception e){
//            System.out.println("Lỗi" + e);
//        }
    }
    private void pushCCCDFront(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String[] part = uriImageSelectionOnDeviceCCCDFront.getLastPathSegment().split("/");
        StorageReference imgRef = storageRef.child("imageShop/" + shopData.getShopPhoneNumber() + "/" + (part[part.length-1]));
//        try {
            UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDFront);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        shopData.setUrlImgCCCDFront(uri.toString());
                        if (!bPushImage){
                            pushCCCDBack();
                            bPushImage = true;
                        }
                    });
                }
            });
//        } catch (Exception e){
//            System.out.println("Lỗi" + e);
//        }
    }
//    void uploadCCCDFront(Uri uriImgLocal) {
//
//
//        /////////////////////////////////////////////////
//        uriImgLocal = uriImageSelectionOnDeviceCCCDFront;
//        System.out.println("sdt shop: " + uriImageSelectionOnDeviceCCCDFront.toString());
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        String[] part = uriImageSelectionOnDeviceCCCDFront.getLastPathSegment().split("/");
//        StorageReference imgRef = storageRef.child("imageShop/" + shopData.getShopPhoneNumber() + "/" + (part[part.length-1]));
//        UploadTask uploadTask = imgRef.putFile(uriImgLocal);
//        uploadTask.addOnCompleteListener(taskSnapshot -> {
//            if (taskSnapshot.isSuccessful()) {
//                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    shopData.setUrlImgCCCDFront(uri.toString());
//                    uploadCCCDBack(uriImageSelectionOnDeviceCCCDBack);
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(context, "Lỗi khi lấy URL hình ảnh: " + e.getMessage(), LENGTH_SHORT).show();
//                    System.out.println("Lỗi khi lấy URL hình ảnh: " + e.getMessage());
//                });
//            } else {
//                Toast.makeText(context, "Lỗi khi tải ảnh lên: " + taskSnapshot.getException().getMessage(), LENGTH_SHORT).show();
//                System.out.println("Lỗi khi tải ảnh lên: " + taskSnapshot.getException().getMessage());
//            }
//        }).addOnFailureListener(e -> {
//            // Bắt lỗi ở đây và in ra thông tin lỗi
//            Toast.makeText(context, "Lỗi khi tải ảnh lên: " + e.getMessage(), LENGTH_SHORT).show();
//            System.out.println("Lỗi khi tải ảnh lên: " + e.getMessage());
//        });

//        uploadTask.addOnCompleteListener(taskSnapshot -> {
//            if (taskSnapshot.isSuccessful()) {
//                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    shopData.setUrlImgCCCDFront(uri.toString());
//                    uploadCCCDBack();
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(context, "Lỗi khi lấy URL hình ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    System.out.println("Lỗi khi lấy URL hình ảnh: " + e.getMessage());
//                });
//            } else {
//                Toast.makeText(context, "Lỗi khi tải ảnh lên: " + taskSnapshot.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                System.out.println("Lỗi khi tải ảnh lên: " + taskSnapshot.getException().getMessage());
//            }
//        });
//    }

//    void uploadCCCDFront() {
//        System.out.println("sdt shop: " + uriImageSelectionOnDeviceCCCDFront.toString());
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        String[] part = uriImageSelectionOnDeviceCCCDFront.getLastPathSegment().split("/");
//        StorageReference imgRWef = storageRef.child("imageShop/" + shopData.getShopPhoneNumber() + "/" + uriImageSelectionOnDeviceCCCDFront.getLastPathSegment());
//        UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDFront);
//        uploadTask.addOnCompleteListener(taskSnapshot -> {
//            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                shopData.setUrlImgCCCDFront(uri.toString());
//                uploadCCCDBack();
//
//            }).addOnFailureListener(e -> {
//                Toast.makeText(context, "Lỗi khi tải ảnh lên" + e.getMessage() , Toast.LENGTH_SHORT).show();
//                System.out.println("loi tai hih anhr: " + e.getMessage());
//            });
//        });
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        StorageReference imgRef = storageReference.child("imageProduct/"+uriImageSelectionOnDeviceCCCDFront.getLastPathSegment());
//        System.out.println("uri 2: "+uriImageSelectionOnDeviceCCCDFront.toString());
//       try {
//           UploadTask uploadTask = imgRef.putFile(uriImageSelectionOnDeviceCCCDFront);
//           uploadTask.addOnCompleteListener(task -> {
//               if (task.isSuccessful()){
//                   imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//
//                   });
//               }
//           });
//       }catch (Exception e){
//           System.out.println("loi uri" + e.getMessage());
//       }
//    }

    void uploadCCCDBack(Uri uriImgLocal){
        uriImgLocal = uriImageSelectionOnDeviceCCCDFront;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String[] part = uriImageSelectionOnDeviceCCCDBack.getLastPathSegment().split("/");
        StorageReference imgRef = storageRef.child("imageShop/" + shopData.getShopPhoneNumber() + "/" + (part[part.length-1]));
        UploadTask uploadTask = imgRef.putFile(uriImgLocal);
        uploadTask.addOnCompleteListener(taskSnapshot -> {
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                shopData.setUrlImgCCCDBack(uri.toString());
                registration();
            }).addOnFailureListener(e -> {
                ShowMessage.showMessage(OtpVerificationRegistrationActivity.this,"Lỗi khi tải ảnh lên: "+ e);
            });
        });
    }



    void registrationSuccessful(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Xác minh tài khoản thành công!", LENGTH_SHORT).show();
                            //pushCCCDFront();

                            registration();
                            //////////////////////// CHUYỂN SANG MÀN HÌNH LOGIN ////////////////////////
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OtpVerificationRegistrationActivity.this, "Sai mã OTP. Vui lòng thử lại!!!", LENGTH_SHORT).show();
                        }
                    }
                });
    }


    void sentOTP(){
        //setInProgress(true);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+shopData.getShopPhoneNumber().substring(1))
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        verificationCode = phoneAuthCredential.getSmsCode();
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        System.out.println("loi: " + e.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Xảy ra lỗi trong quá trình gửi OTP!");
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
                        codesms = s;
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    //Đếm ngược thời gian gửi lại mã
    void resendOTP(){
        btnLayMaOTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                tvTimeResend.setText(timeoutSeconds +" giây");
                if(timeoutSeconds<=0){
                    timeoutSeconds =60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        btnLayMaOTP.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
//    void setInProgress(boolean inProgress){
//        if(inProgress){
//            progressInputOtp.setVisibility(View.VISIBLE);
//            btnXacMinhOTP.setVisibility(View.GONE);
//        } else {
//            progressInputOtp.setVisibility(View.GONE);
//            btnXacMinhOTP.setVisibility(View.VISIBLE);
//        }
//    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void setControl() {
        vOtpVerificationRegistration = findViewById(R.id.vOtpVerificationRegistration);
        edtInputOTP = findViewById(R.id.edtInputOTP);
        btnLayMaOTP = findViewById(R.id.btnLayMaOTP);
        btnXacMinhOTP = findViewById(R.id.btnXacMinhOTP);
        tvTimeResend = findViewById(R.id.tvTimeResend);
        progressInputOtp = findViewById(R.id.progressInputOtp);
    }
}