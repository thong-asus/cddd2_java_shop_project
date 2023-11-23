package com.example.duancuahang;

import static android.view.View.GONE;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.FormatMoneyVietNam;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OrderDetailActivity extends AppCompatActivity {

    Context context;
    Toolbar toolBar_OrderDetail;
    TextView tvOrderDetailStatus, tvOrderDetailID, tvCustomerName_OrderDetail, tvCustomerPhoneNumber_OrderDetail, tvCustomerAddress,
            tvCallCustomer, tvCopyOrderID;
    TextView tvNameProduct, tvAmountProduct_OrderDetail, tvPriceProduct, tvTotal, tvOrderTime, tvNoteContent,
            tvTitleOrderComplete_OrderDetail, tvOrderTimeComplete_OrderDetail, tvTitleOrderCancalled_OrderDetail, tvTimeOrderCancelled_OrderDetail;
    ProgressBar progessBar_loadingImageItem;
    ImageView imgProduct;
    Button btnOrderConfirm, btnOrderCancel;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderData orderData = new OrderData();

    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private ShopData shopData = new ShopData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        context = this;
        //Nhận dữ liệu thông tin đơn hàng
        Intent intent = getIntent();
        if (intent.hasExtra("orderData1")) {
            orderData = (OrderData) intent.getSerializableExtra("orderData1");
            System.out.println("Dữ liệu nhận được tại OrderDetailActivity: " + orderData);
        }

        setControl();
        getInformationOrder();
        getInforShop();
        getOrderStatus(orderData.getStatusOrder() + "");
        getInformationCustomer(orderData.getIdCustomer_Order());
        getInformationProduct(orderData.getIdProduct_Order());
        getImageProduct(orderData.getIdProduct_Order());
        setInitialization();
        setEvent();

    }
    private void getInforShop(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
    }
    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(dialIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, thực hiện cuộc gọi
                makePhoneCall(orderData.getIdCustomer_Order());
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền để thực hiện cuộc gọi.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setEvent() {
        /////////////////////////////////////////////COPY TO CLIPBOARD/////////////////////////////////////////////
        tvCopyOrderID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToCopy = tvOrderDetailID.getText().toString();
                // Sao chép nội dung vào Clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", textToCopy);
                ShowMessage.showMessageTimer(context,"Đã sao chép mã đơn hàng");
                clipboardManager.setPrimaryClip(clipData);
            }
        });
        tvCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(orderData.getIdCustomer_Order());
            }
        });
        /////////////////////////////////////////////END COPY TO CLIPBOARD/////////////////////////////////////////////
        //Sự kiện nút Xác nhận
        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                if(orderData.getStatusOrder()==0) {
                    builder.setMessage("Bạn chắc chắn xác nhận đơn hàng này chứ?");
                }
//                 else if(orderData.getStatusOrder()==1){
//                    builder.setMessage("Bạn chắc chắn xác nhận còn hàng để giao cho đơn hàng này chứ?");
//                }

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cập nhật trạng thái đơn hàng thành chờ lấy hàng
                        updateOrderStatus(shopData.getIdShop(),orderData.getIdOrder(),1);
                        //chuyển về màn hình OrderList
                        Intent intent = new Intent(OrderDetailActivity.this, OrderListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //Sự kiện nút Hủy đơn hàng
        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn chắc chắn hủy đơn hàng này chứ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Lấy thời gian hiện tại hủy đơn bởi shop
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                        Date currentDate = new Date();
                        String currentTime = dateFormat.format(currentDate);
                        ///////////////////////////////////////////////////////////////
                        //updateOrderTimeCancelled(currentTime);
                        //cập nhật trạng thái đơn hàng thành HỦY
                        updateOrderStatus(shopData.getIdShop(),orderData.getIdOrder(),5);
                        updateTimeCancelled(shopData.getIdShop(),orderData.getIdOrder(),currentTime);
                        tvTimeOrderCancelled_OrderDetail.setText(orderData.getOrderTimeCancelled());
                        //chuyển về màn hình OrderList
                        Intent intent = new Intent(OrderDetailActivity.this, OrderListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void updateOrderStatus(String shopId, String orderShopId, int newStatus) {
        DatabaseReference orderShopReference = FirebaseDatabase.getInstance().getReference("OrderShop/" + shopId + "/" + orderShopId);

        orderShopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String idOrder = dataSnapshot.child("idItemOrder").getValue(String.class);

                    if (idOrder != null) {
                        DatabaseReference orderProductReference = FirebaseDatabase.getInstance().getReference("OrderProduct/" + idOrder);
                        orderProductReference.child("statusOrder").setValue(newStatus);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy vấn
            }
        });
    }
    private void updateTimeCancelled(String shopId, String orderShopId, String orderTimeCancelled) {
        DatabaseReference orderShopReference = FirebaseDatabase.getInstance().getReference("OrderShop/" + shopId + "/" + orderShopId);

        orderShopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String idOrder = dataSnapshot.child("idItemOrder").getValue(String.class);

                    if (idOrder != null) {
                        DatabaseReference orderProductReference = FirebaseDatabase.getInstance().getReference("OrderProduct/" + idOrder);
                        orderProductReference.child("orderTimeCancelled").setValue(orderTimeCancelled);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy vấn
            }
        });
    }
    private void getOrderStatus(String statusOrder) {
        databaseReference = firebaseDatabase.getReference("StatusOrder");
        Query query = databaseReference.orderByKey().equalTo(statusOrder);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String status = childSnapshot.getValue(String.class);
                        tvOrderDetailStatus.setText(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInformationOrder() {
        tvOrderDetailID.setText(orderData.getIdOrder());
        tvOrderTime.setText(orderData.getDateOrder());
        tvAmountProduct_OrderDetail.setText(orderData.getQuanlity_Order() + "");
        tvTotal.setText(FormatMoneyVietNam.formatMoneyVietNam(orderData.getPrice_Order()) + "đ");

        //Kiểm tra ghi chú đơn hàng
        if (orderData.getNote_Order() == "") {
            tvNoteContent.setVisibility(GONE);
        } else {
            tvNoteContent.setText(orderData.getNote_Order());
            tvNoteContent.setVisibility(View.VISIBLE);
        }

        //Kiểm tra trạng thái đơn hàng để ẩn hiện nút Xác nhận và Hủy đơn
        if(orderData.getStatusOrder()==1 || orderData.getStatusOrder()==2 || orderData.getStatusOrder()==3 || orderData.getStatusOrder()==4){
            btnOrderConfirm.setVisibility(GONE);
            btnOrderCancel.setVisibility(GONE);
        }

        //Kiểm tra nếu đơn hàng giao thành công thì hiển thị thời gian giao thành công
        if(orderData.getStatusOrder()==4){
            tvOrderTimeComplete_OrderDetail.setText(orderData.getOrderTimeComplete());
            tvTitleOrderComplete_OrderDetail.setVisibility(View.VISIBLE);
            tvOrderTimeComplete_OrderDetail.setVisibility(View.VISIBLE);
        }

        //Kiểm tra nếu đơn đã hủy thì hiển thị thời gian hủy đơn
        if(orderData.getStatusOrder()==5){
            tvTimeOrderCancelled_OrderDetail.setText(orderData.getOrderTimeCancelled());
            tvTitleOrderCancalled_OrderDetail.setVisibility(View.VISIBLE);
            tvTimeOrderCancelled_OrderDetail.setVisibility(View.VISIBLE);
        }
    }

    private void getInformationCustomer(String idCustomer) {
        databaseReference = firebaseDatabase.getReference("Customer/" + idCustomer);
        //Query query = databaseReference.orderByChild("idCustomer").equalTo(idCustomer);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer1 = snapshot.getValue(Customer.class);
                    tvCustomerName_OrderDetail.setText(customer1.getName());
                    tvCustomerPhoneNumber_OrderDetail.setText(customer1.getId());
                    tvCustomerAddress.setText(customer1.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInformationProduct(String idProduct) {
        databaseReference = firebaseDatabase.getReference("Product/"+ shopData.getIdShop() + "/" + idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ProductData productData = snapshot.getValue(ProductData.class);
                    tvNameProduct.setText(productData.getNameProduct());
                    tvPriceProduct.setText(FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct()) + "đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImageProduct(String idProduct) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ImageProducts");

        // Tìm tất cả các child của "ImageProducts" có key là idProduct
        Query query = databaseReference.orderByKey().equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy child đầu tiên (idProduct) từ "ImageProducts"
                    DataSnapshot productSnapshot = snapshot.getChildren().iterator().next();
                    // Duyệt qua danh sách các ảnh của sản phẩm
                    for (DataSnapshot imageSnapshot : productSnapshot.getChildren()) {
                        Image image = imageSnapshot.getValue(Image.class);
                        // Kiểm tra xem ảnh có URL hay không
                        if (image != null && image.getUrlImage() != null) {
                            Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(imgProduct);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setInitialization() {
        //Kích hoạt nút back
        setSupportActionBar(toolBar_OrderDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        toolBar_OrderDetail = findViewById(R.id.toolBar_OrderDetail);
        tvOrderDetailStatus = findViewById(R.id.tvOrderDetailStatus);
        tvOrderDetailID = findViewById(R.id.tvOrderDetailID);
        tvCustomerName_OrderDetail = findViewById(R.id.tvCustomerName_OrderDetail);
        tvCustomerPhoneNumber_OrderDetail = findViewById(R.id.tvCustomerPhoneNumber_OrderDetail);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress_OrderDetail);
        tvNameProduct = findViewById(R.id.tvNameProduct_OrderDetail);
        tvAmountProduct_OrderDetail = findViewById(R.id.tvAmountProduct_OrderDetail);
        tvPriceProduct = findViewById(R.id.tvPriceProduct_OrderDetail);
        tvTotal = findViewById(R.id.tvTotal_OrderDetail);
        tvNoteContent = findViewById(R.id.tvNoteContent_OrderDetail);
        progessBar_loadingImageItem = findViewById(R.id.progessBar_loadingImageItem_OrderDetail);
        imgProduct = findViewById(R.id.imgProduct_OrderDetail);
        btnOrderConfirm = findViewById(R.id.btnOrderConfirm_OrderDetail);
        btnOrderCancel = findViewById(R.id.btnOrderCancel_OrderDetail);

        //thời gian đặt hàng
        tvOrderTime = findViewById(R.id.tvOrderTime_OrderDetail);

        //đơn giao thành công
        tvTitleOrderComplete_OrderDetail = findViewById(R.id.tvTitleOrderComplete_OrderDetail);
        tvOrderTimeComplete_OrderDetail = findViewById(R.id.tvOrderTimeComplete_OrderDetail);

        //đơn hủy
        tvTimeOrderCancelled_OrderDetail = findViewById(R.id.tvTimeOrderCancelled_OrderDetail);
        tvTitleOrderCancalled_OrderDetail = findViewById(R.id.tvTitleOrderCancalled_OrderDetail);
        //sao chép
        tvCopyOrderID = findViewById(R.id.tvCopyOrderID);
        tvCallCustomer = findViewById(R.id.tvCallCustomer);
    }
}