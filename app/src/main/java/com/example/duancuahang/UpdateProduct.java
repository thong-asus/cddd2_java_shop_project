package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Dialog.UpdateDescriptionProduct;
import com.example.duancuahang.Dialog.UpdateNameProductDialog;
import com.example.duancuahang.Dialog.UpdatePriceProductDialog;
import com.example.duancuahang.Dialog.UpdateQuanlityProductDialog;
import com.example.duancuahang.RecyclerView.UriImageFirebase_ViewHolder;
import com.example.duancuahang.RecyclerView.UrlImageFirebase_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UpdateProduct extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<Image> arrImage = new ArrayList<>();
    UrlImageFirebase_Adapter urlImageFirebaseAdapter;
    Context context;
    private Category categoryProduct;
    private Manuface manufaceProduct;
    ProductData productData = new ProductData();
    Toolbar toolbar_UpdatePRoduct;
    ImageView ivProductBig_UpdateProduct;
    RecyclerView rcvImageProduct_UpdateProduct;
    TextView tvNameProduct_UpdateProduct,tvCategpory_UpdateProduct,tvManuface_UpdateCategory,tvDescription_UpdateProduct,tvPrice_UpdateProduct,tvQuanlityProduct_UpdateProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        context = this;
        setControl();
        setIntiazation();
        setCategory(productData.getKeyCategoryProduct());
        setManuface(productData.getKeyManufaceProduct());
        setImageProduct();
        setEvent();
    }
    //    lấy Category dựa vào id
    private void setCategory(String idCategory) {
        databaseReference = firebaseDatabase.getReference("Category");
        Query query = databaseReference.orderByChild("idCategory").equalTo(idCategory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String idCategory = categorySnapshot.child("idCategory").getValue().toString();
                    String nameCategory = categorySnapshot.child("nameCategory").getValue().toString();
                    categoryProduct = new Category(idCategory, nameCategory);
                    tvCategpory_UpdateProduct.setText(categoryProduct.getNameCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm lấy manuface ra
    private void setManuface(String idManuface) {
        databaseReference = firebaseDatabase.getReference("Manuface");
        Query query = databaseReference.orderByChild("idManuface").equalTo(idManuface);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String idManuface1 = categorySnapshot.child("idManuface").getValue().toString();
                    String nameManuface = categorySnapshot.child("nameManuface").getValue().toString();
                    String idCategoryt_manuface = categorySnapshot.child("keyManuface_Category").getValue().toString();
                    manufaceProduct = new Manuface(idManuface1, idManuface1, nameManuface);
                    tvManuface_UpdateCategory.setText(manufaceProduct.getNameManuface());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setImageProduct() {
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        String idProduct = productData.getIdProduct();
        Query query = databaseReference.orderByKey().equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lặp qua danh sách các hình ảnh của sản phẩm
                    for (DataSnapshot productImages : snapshot.getChildren()) {
                        for (DataSnapshot imageItem : productImages.getChildren()) {
                            Image image = imageItem.getValue(Image.class);
                            arrImage.add(image);
                        }
                        // Hiển thị hình ảnh đầu tiên trong danh sách
                        if (!arrImage.isEmpty()) {
                            urlImageFirebaseAdapter.notifyDataSetChanged();
                            Picasso.get().load(arrImage.get(0).getUrlImage()).into(ivProductBig_UpdateProduct);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi có
            }
        });
    }

    private void setIntiazation() {
        Intent intent = getIntent();
        productData = (ProductData) intent.getSerializableExtra("Product");

        urlImageFirebaseAdapter = new UrlImageFirebase_Adapter(arrImage,context);
        rcvImageProduct_UpdateProduct.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        rcvImageProduct_UpdateProduct.setAdapter(urlImageFirebaseAdapter);

        setSupportActionBar(toolbar_UpdatePRoduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNameProduct_UpdateProduct.setText(productData.getNameProduct());
        tvDescription_UpdateProduct.setText(productData.getDescriptionProduct());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
        tvPrice_UpdateProduct.setText(numberFormatVND.format(productData.getPriceProduct()));
        tvQuanlityProduct_UpdateProduct.setText(productData.getQuanlityProduct() + "");
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        toolbar_UpdatePRoduct = findViewById(R.id.toolbar_UpdatePRoduct);
        ivProductBig_UpdateProduct = findViewById(R.id.ivProductBig_UpdateProduct);
        rcvImageProduct_UpdateProduct = findViewById(R.id.rcvImageProduct_UpdateProduct);
        tvNameProduct_UpdateProduct = findViewById(R.id.tvNameProduct_UpdateProduct);
        tvCategpory_UpdateProduct = findViewById(R.id.tvCategpory_UpdateProduct);
        tvManuface_UpdateCategory = findViewById(R.id.tvManuface_UpdateCategory);
        tvDescription_UpdateProduct = findViewById(R.id.tvDescription_UpdateProduct);
        tvPrice_UpdateProduct = findViewById(R.id.tvPrice_UpdateProduct);
        tvQuanlityProduct_UpdateProduct = findViewById(R.id.tvQuanlityProduct_UpdateProduct);
    }

    private void setEvent() {
        urlImageFirebaseAdapter.setOnclickListener(new UrlImageFirebase_Adapter.OnclickListener() {
            @Override
            public void onItemClick(UriImageFirebase_ViewHolder listImageProductViewHolder, int position) {
                Image image = arrImage.get(position);
                Picasso.get().load(image.getUrlImage()).into(ivProductBig_UpdateProduct);
            }
        });
        tvNameProduct_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateNameProductDialog.ChangeNameProduct changeNameProduct = new UpdateNameProductDialog.ChangeNameProduct() {
                    @Override
                    public void getNameProductNew(String nameProductNew) {
                        productData.setNameProduct(nameProductNew);
                        tvNameProduct_UpdateProduct.setText(productData.getNameProduct());
                    }
                };
                UpdateNameProductDialog updateNameProductDialog = new UpdateNameProductDialog(productData,changeNameProduct);
                updateNameProductDialog.show(getSupportFragmentManager(),"Sửa tên sản phẩm");

            }
        });
        tvDescription_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDescriptionProduct.ChangeDescriptionProduct changeDescriptionProduct = new UpdateDescriptionProduct.ChangeDescriptionProduct() {
                    @Override
                    public void getDescriptionChange(String description) {
                        productData.setDescriptionProduct(description);
                    }
                };
                UpdateDescriptionProduct updateDescriptionProduct = new UpdateDescriptionProduct(productData,changeDescriptionProduct);
                updateDescriptionProduct.show(getSupportFragmentManager(),"Sửa mô tả sản phẩm");
            }
        });
        tvPrice_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePriceProductDialog.ChangePriceProduct changePriceProduct = new UpdatePriceProductDialog.ChangePriceProduct() {
                    @Override
                    public void getPriceProductNew(int price) {
                        productData.setPriceProduct(price);
                        Locale locale = new Locale("vi", "VN");
                        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
                        tvPrice_UpdateProduct.setText(numberFormatVND.format(productData.getPriceProduct()));

                    }
                };
                UpdatePriceProductDialog updatePriceProductDialog = new UpdatePriceProductDialog(productData,changePriceProduct);
                updatePriceProductDialog.show(getSupportFragmentManager(),"Sửa giá sản phẩm");
            }
        });
        tvQuanlityProduct_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateQuanlityProductDialog.ChangeQuanlityProduct changeQuanlityProduct = new UpdateQuanlityProductDialog.ChangeQuanlityProduct() {
                    @Override
                    public void getQuanlityProductNew(int quanlity) {
                        productData.setQuanlityProduct(quanlity);
                        tvQuanlityProduct_UpdateProduct.setText(productData.getQuanlityProduct() +"");
                    }
                };
                UpdateQuanlityProductDialog updateQuanlityProductDialog = new UpdateQuanlityProductDialog(productData,changeQuanlityProduct);
                updateQuanlityProductDialog.show(getSupportFragmentManager(),"Sửa số lượng sản phẩm");
            }
        });
    }
}