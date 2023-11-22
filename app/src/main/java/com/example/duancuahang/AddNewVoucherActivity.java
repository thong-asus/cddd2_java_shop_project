package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.duancuahang.Adapter.PercentVoucherSpinnerAdapter;
import com.example.duancuahang.Class.PercentDiscount;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.Class.Voucher;
import com.example.duancuahang.RecyclerView.Voucher_ChooseItemAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class AddNewVoucherActivity extends AppCompatActivity {

    Toolbar toolbar_AddNewVoucher;
    Context context;
    DatabaseReference databaseReference;
    Voucher_ChooseItemAdapter voucherChooseItemAdapter;
    RecyclerView rcvChooseProduct;
    Spinner spChoosePercent;
    EditText edtMaxUsage, edtVoucher;
    Button btnSaveVoucher;
    private ShopData shopData = new ShopData();
    ArrayList<ProductData> arrayProductData = new ArrayList<>();

    ArrayList<Voucher> arrVoucher = new ArrayList<>();
    PercentVoucherSpinnerAdapter percentVoucherSpinnerAdapter;
    private PercentDiscount percentDiscountSelection = null;
    private boolean errAddVoucher = false;
    ArrayList<PercentDiscount> arrPercentDiscount = new ArrayList<>();
    ArrayList<ProductData> arrProductSelectedVoucher = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_voucher);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        context = this;
        setControl();
        setIntiazation();
        getPercentDiscount();
        getAllProduct();
        setEvent();
    }

    private void setEvent() {
        spChoosePercent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Không lấy dòng vui lòng chọn danh mục
                if (i != 0) {
                    percentDiscountSelection = arrPercentDiscount.get(i);
//                    Gọi đến hàm lấy danh sách manuface dựa vào category vừa chọn
                } else {
                    percentDiscountSelection = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSaveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateInput()){
                    pushDataVoucherToFirebase();
                }
            }
        });
    }

    private void pushDataVoucherToFirebase(){
        for (ProductData itemProduct:
             arrProductSelectedVoucher) {
            if (!errAddVoucher){
                Voucher voucher = new Voucher(itemProduct.getIdProduct(),Integer.parseInt(edtMaxUsage.getText().toString()),edtVoucher.getText().toString(),percentDiscountSelection.getKeyPercentDiscount(),itemProduct.getIdProduct());
                databaseReference = FirebaseDatabase.getInstance().getReference("Voucher");
                databaseReference.child(itemProduct.getIdProduct()).setValue(voucher).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowMessage.showMessageTimer(context,"Loi khi them voucher");
                        errAddVoucher = true;
                    }
                });
            }
            else {
                return;
            }

        }
    }
    private boolean isValidateInput(){
        if ( edtMaxUsage.getText().toString() == null || edtMaxUsage.getText().toString().isEmpty()){
            edtMaxUsage.setError("Vui lòng nhập số lượt sử dụng tối đa");
            return false;
        }
       else if (edtVoucher.getText().toString() == null || edtVoucher.getText().toString().isEmpty()){
            edtVoucher.setError("Vui lòng nhập mã giảm giá");
            return false;
        }
       else if (percentDiscountSelection == null){
            ShowMessage.showMessageTimer(context,"Vui long chon % giam gia");
            return false;
        }
       else if (percentDiscountSelection.getKeyPercentDiscount() == null){
            ShowMessage.showMessageTimer(context,"Vui long chon % giam gia");
            return false;
        }
       else if (arrProductSelectedVoucher.size() < 1){
            ShowMessage.showMessageTimer(context,"Vui lòng chọn sản phẩm cần tạo Voucher");
            return  false;
        }
        return  true;

    }


    private void setIntiazation() {
        ////////////////////////////////////////SET ADAPTER CHO SPINNER %
        percentVoucherSpinnerAdapter = new PercentVoucherSpinnerAdapter(arrPercentDiscount, context);
        spChoosePercent.setAdapter(percentVoucherSpinnerAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Voucher_ChooseItemAdapter.SelectionProductVoucher selectionProductVoucher = new Voucher_ChooseItemAdapter.SelectionProductVoucher() {
            @Override
            public void getProductSelected(ProductData productData) {
                arrProductSelectedVoucher.add(productData);
            }

            @Override
            public void getProductNotSelected(ProductData productData) {
                arrProductSelectedVoucher.removeIf(element -> element.getIdProduct().equals(productData.getIdProduct()));
            }
        };
        voucherChooseItemAdapter = new Voucher_ChooseItemAdapter(arrayProductData,context,selectionProductVoucher);
        rcvChooseProduct.setLayoutManager(new LinearLayoutManager(this));
        rcvChooseProduct.setAdapter(voucherChooseItemAdapter);
        //Kích hoạt nút back
        setSupportActionBar(toolbar_AddNewVoucher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getPercentDiscount() {
        arrPercentDiscount.clear();
        PercentDiscount defaultDiscount = new PercentDiscount(null, 0);
        arrPercentDiscount.add(defaultDiscount);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PercentVoucher");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot percentDiscount : snapshot.getChildren()) {
                    PercentDiscount percentDiscount1 = percentDiscount.getValue(PercentDiscount.class);
                    arrPercentDiscount.add(percentDiscount1);
                }
                    percentVoucherSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void getAllProduct() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product/"+shopData.getIdShop());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayProductData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        ProductData productData = productSnapshot.getValue(ProductData.class);
                        arrayProductData.add(productData);
                    }
                }
                voucherChooseItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy sản phẩm");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setControl() {
        toolbar_AddNewVoucher = findViewById(R.id.toolbar_AddNewVoucher);
        rcvChooseProduct = findViewById(R.id.rcvChooseProduct);
        spChoosePercent = findViewById(R.id.spChoosePercent);
        edtMaxUsage = findViewById(R.id.edtMaxUsage);
        edtVoucher = findViewById(R.id.edtVoucher);
        btnSaveVoucher = findViewById(R.id.btnSaveVoucher);
    }
}