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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.duancuahang.Adapter.ActionToGetVoucherSpinnerAdapter;
import com.example.duancuahang.Adapter.PercentVoucherSpinnerAdapter;
import com.example.duancuahang.Class.ActionToGetVoucher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.stream.StreamSupport;


public class AddNewVoucherActivity extends AppCompatActivity {

    Toolbar toolbar_AddNewVoucher;
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Voucher_ChooseItemAdapter voucherChooseItemAdapter;
    RecyclerView rcvChooseProduct;
    Spinner spChoosePercent, spActionHaveVoucher;
    EditText edtMaxUsage, edtVoucher;
    Button btnSaveVoucher;
    LinearLayout vLoadingAddNewVoucher;
    private ShopData shopData = new ShopData();
    private int positionActionToGetVoucherSelection = -1;
    ArrayList<ProductData> arrayProductData = new ArrayList<>();
    PercentVoucherSpinnerAdapter percentVoucherSpinnerAdapter;
    ActionToGetVoucherSpinnerAdapter actionToGetVoucherSpinnerAdapter;
    private PercentDiscount percentDiscountSelection = null;
    private boolean errAddVoucher = false;
    private boolean loadingAddVoucher = false;
    ArrayList<PercentDiscount> arrPercentDiscount = new ArrayList<>();
    ArrayList<ProductData> arrProductSelectedVoucher = new ArrayList<>();
    ArrayList<ActionToGetVoucher> arrActionToGetVoucher = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_voucher);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop", "");
        Gson gson = new Gson();
        shopData = gson.fromJson(jsonShop, ShopData.class);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        context = this;
        setControl();
        setIntiazation();
        getPercentDiscount();
        getAllProduct();
        getActionToGetVoucher();
        setEvent();
    }

    //    lấy danh sách hàng động để có voucher
    private void getActionToGetVoucher() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TakeActionToGetVoucher");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrActionToGetVoucher.add(new ActionToGetVoucher(null, "Vui lòng chọn hành động"));
                    for (DataSnapshot item :
                            snapshot.getChildren()) {
                        ActionToGetVoucher actionToGetVoucher = item.getValue(ActionToGetVoucher.class);
                        arrActionToGetVoucher.add(actionToGetVoucher);
                        actionToGetVoucherSpinnerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        spActionHaveVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionActionToGetVoucherSelection = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSaveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateInput()) {
                    vLoadingAddNewVoucher.setVisibility(View.VISIBLE);
                    pushDataVoucherToFirebase();
                }
            }
        });
    }

    private void pushDataVoucherToFirebase() {
        for (ProductData itemProduct :
                arrProductSelectedVoucher) {
            System.out.println("size : " + arrProductSelectedVoucher.size());
            if (!errAddVoucher) {
//                check voucher có hàng động như vậy tại sản phẩm đó đã có chưa
                DatabaseReference databaseReference = firebaseDatabase.getReference("Voucher/"+itemProduct.getIdUserProduct()+"/"+itemProduct.getIdProduct());
                Query query = databaseReference.orderByChild("idActionToGetVoucher").equalTo(arrActionToGetVoucher.get(positionActionToGetVoucherSelection).getIdAction());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = databaseReference.push().toString().substring(databaseReference.push().toString().lastIndexOf("/"));
                        key = key.substring(1);
                        Voucher voucher = new Voucher(key,Integer.parseInt(edtMaxUsage.getText().toString()),edtVoucher.getText().toString(),percentDiscountSelection.getKeyPercentDiscount(),itemProduct.getIdProduct(),arrActionToGetVoucher.get(positionActionToGetVoucherSelection).getIdAction(),itemProduct.getIdUserProduct());
                        if (snapshot.exists()){
                            System.out.println("Ton tai: " + snapshot.getValue().toString());
                            for (DataSnapshot item:
                                 snapshot.getChildren()) {
                              key = item.getKey();
                              databaseReference.child(key).setValue(voucher).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      ShowMessage.showMessageTimer(context,"Lỗi khi thêm Voucher");
                                      errAddVoucher = true;
                                  }
                              });
                              return;
                            }
                        }
                        else {
                            System.out.println("khong ton tai");
                            databaseReference.child(key).setValue(voucher).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ShowMessage.showMessageTimer(context,"Lỗi khi thêm Voucher");
                                    errAddVoucher = true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                Voucher voucher = new Voucher(itemProduct.getIdProduct(), Integer.parseInt(edtMaxUsage.getText().toString()), edtVoucher.getText().toString(), percentDiscountSelection.getKeyPercentDiscount(), itemProduct.getIdProduct(), arrActionToGetVoucher.get(positionActionToGetVoucherSelection).getIdAction());
//                DatabaseReference databaseReference1 = firebaseDatabase.getReference("Voucher/" + itemProduct.getIdUserProduct() + "/" + itemProduct.getIdProduct());
//                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            databaseReference1.setValue(voucher);
//                        }
//                        else {
//                            databaseReference1.setValue(voucher);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            } else {
                return;
            }

        }
        finish();
    }

    private boolean isValidateInput() {
        if (edtMaxUsage.getText().toString() == null || edtMaxUsage.getText().toString().isEmpty()) {
            edtMaxUsage.setError("Vui lòng nhập số lượt sử dụng tối đa");
            return false;
        } else if (edtVoucher.getText().toString() == null || edtVoucher.getText().toString().isEmpty()) {
            edtVoucher.setError("Vui lòng nhập mã giảm giá");
            return false;
        } else if (percentDiscountSelection == null) {
            ShowMessage.showMessageTimer(context, "Vui long chon % giam gia");
            return false;
        } else if (percentDiscountSelection.getKeyPercentDiscount() == null) {
            ShowMessage.showMessageTimer(context, "Vui long chon % giam gia");
            return false;
        } else if (arrProductSelectedVoucher.size() < 1) {
            ShowMessage.showMessageTimer(context, "Vui lòng chọn sản phẩm cần tạo Voucher");
            return false;
        } else if (positionActionToGetVoucherSelection < 0) {
            ShowMessage.showMessageTimer(context, "Vui lòng chọn hành động để người dùng có được Voucher");
            return false;
        }
        return true;

    }


    private void setIntiazation() {
        ////////////////////////////////////////SET ADAPTER CHO SPINNER %
        percentVoucherSpinnerAdapter = new PercentVoucherSpinnerAdapter(arrPercentDiscount, context);
        spChoosePercent.setAdapter(percentVoucherSpinnerAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        actionToGetVoucherSpinnerAdapter = new ActionToGetVoucherSpinnerAdapter(arrActionToGetVoucher, context);
        spActionHaveVoucher.setAdapter(actionToGetVoucherSpinnerAdapter);

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
        voucherChooseItemAdapter = new Voucher_ChooseItemAdapter(arrayProductData, context, selectionProductVoucher);
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
        DatabaseReference databaseReference = firebaseDatabase.getReference("Product/" + shopData.getIdShop());
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
        if (item.getItemId() == android.R.id.home) {
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
        spActionHaveVoucher = findViewById(R.id.spActionHaveVoucher);
        vLoadingAddNewVoucher = findViewById(R.id.vLoadingAddNewVoucher);
    }
}