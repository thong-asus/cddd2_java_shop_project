package com.example.duancuahang.FireBaseAuthenticator;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.HomeShop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseAuthenticator {
    private static Context context;
    private static DatabaseReference accountRef;
    public FireBaseAuthenticator(Context context) {
        this.context = context;
    }

    public static void login(final String shopPhoneNumber, final String password) {
        //Kiểm tra số điện thoại hoặc mật khẩu có bị bỏ trống hay không
        if(shopPhoneNumber.isEmpty() || password.isEmpty()){
            ShowMessage.showMessage(context,"Thông tin đăng nhập không được bỏ trống!!!");
            return;
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        accountRef = firebaseDatabase.getReference("Shop");
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Biến xác định tài khoản có tồn tại hay không?
                boolean found = false;

                if (snapshot.exists()) {
                    for (DataSnapshot accountShopSnapshot : snapshot.getChildren()) {
                        ShopData shopAccount = accountShopSnapshot.getValue(ShopData.class);
                        if (shopAccount != null && shopAccount.getShopPhoneNumber().equals(shopPhoneNumber)) {

                            //Tạo biến
                            int storedStatus = shopAccount.getStatus();
                            String storedPassword = shopAccount.getPassword();

                            // Kiểm tra trạng thái tài khoản
                            if (storedStatus == 0 && storedPassword.equals(password)) {
                                ShowMessage.showMessage(context,"Tài khoản đang chờ ADMIN duyệt đăng ký. Vui lòng chờ thông báo!");
                                return;
                            } else if (storedStatus == 2 && storedPassword.equals(password)) {
                                ShowMessage.showMessage(context,"Tài khoản đang bị khóa!!!");
                                return;
                            } else {
                                // Kiểm tra mật khẩu
                                if (storedPassword.equals(password)) {
                                    // Đăng nhập thành công, chuyển sang màn hình HomeShop
                                    Intent intent = new Intent(context, HomeShop.class);
                                    context.startActivity(intent);
                                    return;
                                } else {
                                    ShowMessage.showMessage(context,"Sai mật khẩu!!!");
                                }
                            }
                            found = true;
                        }
                    }
                    // Không tìm thấy tài khoản
                    if(!found){
                        ShowMessage.showMessage(context,"Tài khoản không tồn tại!!!");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ShowMessage.showMessage(context,"Lỗi không truy vấn được dữ liệu");
            }
        });
    }
}
