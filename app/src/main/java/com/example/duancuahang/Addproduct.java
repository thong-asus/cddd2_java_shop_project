package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.duancuahang.Adapter.CategorySpinerAdapter;
import com.example.duancuahang.Adapter.ManufacSpinerAdapter;
import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.Manuface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Addproduct extends AppCompatActivity {

    Spinner spCategory,spManuface_AddProduct;
    private DatabaseReference databaseReference;
    ArrayList<Category> arrCategory = new ArrayList<>();
    CategorySpinerAdapter categorySpinnerAdapter;
    ArrayList<Manuface> arrManuface = new ArrayList<>();
    ManufacSpinerAdapter manufaceSpinerAdapter;


    Category categorySelection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        setControl();
        setIntiazation();
        getCategory();
        setEvent();
    }

//    hàm khởi tạo
    private void setIntiazation() {
//        ---------------- khai báo Adapter cho spinner danh mục sản phẩm
//        Category thoong báo yêu cầu người dùng chọn
        Category category = new Category("null","null","Vui lòng chọn danh mục");
        arrCategory.add(category);
        categorySpinnerAdapter = new CategorySpinerAdapter(arrCategory,this);
        spCategory.setAdapter(categorySpinnerAdapter);

//        ------------------- Khai báo Adapter spinner cho hãng sản xuất
        Manuface manuface = new Manuface(null,null,"Vui lòng chọn hãng sản xuất",null);
        arrManuface.add(manuface);
        manufaceSpinerAdapter = new ManufacSpinerAdapter(arrManuface,this);
        spManuface_AddProduct.setAdapter(manufaceSpinerAdapter);



    }

//    hàm bắt sự kiện
    private void setEvent() {

//        Bắt sự kiện chọn item trong spiner category
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Không lấy dòng vui lòng chọn danh mục
                if (i != 0){
                   categorySelection = arrCategory.get(i);
//                    Gọi đến hàm lấy danh sách manuface dựa vào category vừa chọn
                    getManuface();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//hàm ánh xạ
    private void setControl() {
        spCategory = findViewById(R.id.spCategory_AddProduct);
        spManuface_AddProduct = findViewById(R.id.spManuface_AddProduct);
    }

//    hàm lấy danh sách danh mục sản phẩm -----------------Category
    private void getCategory(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   for (DataSnapshot category:snapshot.getChildren()){
                       String ketCategoryItem = category.getKey();
                       String idCategoryChildItem = category.child("idCategory").getValue().toString();
                       String nameCategoryChildItem = category.child("nameCategory").getValue().toString();
                       Category categoryItem = new Category(ketCategoryItem,idCategoryChildItem,nameCategoryChildItem);
                       arrCategory.add(categoryItem);
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

//    hàm lấy danh sách hãng sản xuất --------------- Manuface
    private void getManuface(){
        arrManuface.clear();
        Manuface manuface = new Manuface(null,null,"Vui lòng chọn hãng sản xuất",null);
        arrManuface.add(manuface);
        manufaceSpinerAdapter.notifyDataSetChanged();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Manuface");
        Query databaseReferenceQuey = databaseReference.orderByChild("keyManuface_Category").equalTo(categorySelection.getKeyCategoryItem());
        databaseReferenceQuey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot manuface_Category:snapshot.getChildren()){
                  String keyManufaceItem = manuface_Category.getKey();
                  String idManuface = manuface_Category.child("idManuface").getValue().toString();
                  String nameManuface = manuface_Category.child("nameManuface").getValue().toString();
                  String keyManuface_Category = manuface_Category.child("keyManuface_Category").getValue().toString();
                    Manuface manuface = new Manuface(keyManufaceItem,idManuface,nameManuface,keyManuface_Category);
                    arrManuface.add(manuface);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}