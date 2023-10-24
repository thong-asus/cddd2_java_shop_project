package com.example.duancuahang.FireBaseAuthenticator;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.duancuahang.Class.Shop;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.HomeShop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FireBaseAuthenticator {
    final Context context;
    public static boolean bPushImage = false;
    public static String urlImageSelected;

    public FireBaseAuthenticator(Context context) {
        this.context = context;
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
}
