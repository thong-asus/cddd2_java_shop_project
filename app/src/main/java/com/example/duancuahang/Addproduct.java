package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.duancuahang.Adapter.CategorySpinerAdapter;
import com.example.duancuahang.Adapter.ManufacSpinerAdapter;
import com.example.duancuahang.Class.Category;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import id.zelory.compressor.constraint.Compression;

public class Addproduct extends AppCompatActivity {

    //    Biến giao diện
    Spinner spCategory, spManuface_AddProduct;
    LinearLayout vMain_AddProduct;
    ImageView ivProduct_addProduct;
    Button btnAddProduct;

    EditText edtNameProduct, edtDescriptionProduct, edtPriceProduct, edtQuanlityProduct;
    ProgressBar progressBar;

    Toolbar toolBar_AddProduct;

    //    Biến xử lý
    private DatabaseReference databaseReference;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private String urlImageSelection;
    private Category categorySelection = null;
    private Manuface manufaceSelection = null;
    private  boolean loading = true;
    private boolean bPushImage = false;

    Uri uriImageSelectionOnDevice = null;

    //    Biến xử lý giao diện
    ArrayList<Category> arrCategory = new ArrayList<>();
    CategorySpinerAdapter categorySpinnerAdapter;
    ArrayList<Manuface> arrManuface = new ArrayList<>();
    ManufacSpinerAdapter manufaceSpinerAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        context = this;
        setControl();
        setIntiazation();
        getCategory();
        setEvent();
    }

    //    hàm khởi tạo
    private void setIntiazation() {
//        ---------------- khai báo Adapter cho spinner danh mục sản phẩm
//        Category thoong báo yêu cầu người dùng chọn
        Category category = new Category("null", "null", "Vui lòng chọn danh mục");
        arrCategory.add(category);
        categorySpinnerAdapter = new CategorySpinerAdapter(arrCategory, this);
        spCategory.setAdapter(categorySpinnerAdapter);

//        ------------------- Khai báo Adapter spinner cho hãng sản xuất
        Manuface manuface = new Manuface(null, null, "Vui lòng chọn hãng sản xuất", null);
        arrManuface.add(manuface);
        manufaceSpinerAdapter = new ManufacSpinerAdapter(arrManuface, this);
        spManuface_AddProduct.setAdapter(manufaceSpinerAdapter);

//        -------------------------- kích hoạt button back
        setSupportActionBar(toolBar_AddProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //    hàm bắt sự kiện
    private void setEvent() {

//        Bắt sự kiện chọn category cho sản phẩm
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Không lấy dòng vui lòng chọn danh mục
                if (i != 0) {
                    categorySelection = arrCategory.get(i);
//                    Gọi đến hàm lấy danh sách manuface dựa vào category vừa chọn
                    getManuface();
                } else {
                    categorySelection = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        bắt sự kiện chọn manuface cho sản phẩm
        spManuface_AddProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    manufaceSelection = arrManuface.get(i);
                } else {
                    manufaceSelection = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        bắt sự kiện ẩn bàn phím nếu người dùng nhấn vào bên ngoài
        vMain_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });

//        bắt sự kiện người dùng nhấn vào hình ảnh sản phẩm
        ivProduct_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveLibraryImage();
            }
        });

//        bắt sự kiện nhấn vào button thêm sản phẩm
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (checkValueProductBeforeAdd()){
                       AlertDialog.Builder builder = new AlertDialog.Builder(context);
                       builder.setTitle("Thông báo");
                       builder.setMessage("Bạn có muốn thêm sản phẩm này không ?");
                       builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               loading = true;
                               progressBar.setVisibility(View.VISIBLE);
                               vMain_AddProduct.setBackgroundColor(Color.parseColor("#80000000"));
                               hideKeyboard();
                               pushImageProductToFirebaseStorage(uriImageSelectionOnDevice);
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
            }
        });

//        hàm bắt sự kiện của editText name produc
        edtNameProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    if (Validates.getCheckValueString_Normal(edtNameProduct.getText().toString())){
                        edtDescriptionProduct.requestFocus();
                    }
                    else {
                        edtNameProduct.setError("Tên sản phẩm không hợp lệ");
                    }
                }
                return false;
            }
        });
//      hàm bắt xự kiện edtitText giá sản phẩm =====> phím done
        edtPriceProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    if (Validates.getCheckValueNumber(edtPriceProduct.getText().toString())){
                        edtQuanlityProduct.requestFocus();
                    }
                    else {
                        edtPriceProduct.setError("Giá không hợp lệ");
                    }
                }
                return false;
            }
        });

//        bắt sự kiện phím done số lượng
        edtQuanlityProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    if (!Validates.getCheckValueNumber(edtQuanlityProduct.getText().toString())){
                        edtQuanlityProduct.setError("Số lượng không hợp lệ");
                    }
                }
                return false;
            }
        });

    }

    //hàm ánh xạ
    private void setControl() {
        spCategory = findViewById(R.id.spCategory_AddProduct);
        spManuface_AddProduct = findViewById(R.id.spManuface_AddProduct);
        vMain_AddProduct = findViewById(R.id.vMain_AddProduct);
        ivProduct_addProduct = findViewById(R.id.ivProduct_addProduct);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        edtNameProduct = findViewById(R.id.edtNameProduct_AddProduct);
        edtDescriptionProduct = findViewById(R.id.edtDescriptionProduct_AddProduct);
        edtPriceProduct = findViewById(R.id.edtPriceProduct_AddProduct);
        edtQuanlityProduct = findViewById(R.id.edtQuanlityProduct_AddProduct);
        progressBar = findViewById(R.id.progessbar_AddProduct);
        toolBar_AddProduct = findViewById(R.id.toolBar_AddProduct);
    }

//    bắt sự kiện nút back trong toolbar


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //    hàm set id product
    private String getIdProductRan() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND);
    }

    private void AddProduct() {
        String keyProductItem = getIdProductRan();
        pushImageProductToFirebaseStorage(uriImageSelectionOnDevice);
        ProductData productData = new ProductData(keyProductItem,
                edtNameProduct.getText().toString(),urlImageSelection,
                Integer.valueOf(edtPriceProduct.getText().toString()),
                categorySelection.getKeyCategoryItem(), manufaceSelection.getKeyManufaceItem(),
                Integer.valueOf(edtQuanlityProduct.getText().toString()),
                edtDescriptionProduct.getText().toString(), 0, 0);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Product").child(keyProductItem).setValue(productData);
        loading = false;
       if (!loading){
           finish();
       }

    }

    private boolean checkValueProductBeforeAdd() {
        if (edtNameProduct.getText().toString() == null ||
                edtDescriptionProduct.getText().toString() == null ||
                edtPriceProduct.getText().toString() == null ||
                edtQuanlityProduct.getText().toString() == null ||
                categorySelection == null ||
                manufaceSelection == null|| uriImageSelectionOnDevice == null
               ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Vui lòng cung cấp đủ thông tin sản phẩm trước khi thêm vào");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;

        }
        return true;
    }


    // hàm chuyển màn hình sang màn hình thư viện ảnh
    private void moveLibraryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        System.out.println("4");
    }

//    hàm tải hình ảnh lên firebase
    private void pushImageProductToFirebaseStorage(Uri uriImageLocalDevice){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imgRef = storageReference.child("imageProduct/"+uriImageLocalDevice.getLastPathSegment());

        UploadTask uploadTask = imgRef.putFile(uriImageLocalDevice);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                urlImageSelection = uri.toString();
                if (!bPushImage){
                    AddProduct();
                    bPushImage = true;
                }
            });
            }
        });
    }

//    hàm nén ảnh

    //    hàm hiển thị kết quả ảnh vừa chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_PICK_IMAGE || resultCode == RESULT_OK) {
            Uri uriImage = data.getData();
            String mediaType = getContentResolver().getType(uriImage);
            if (mediaType != null) {
                if (mediaType.startsWith("image/")) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriImage);
                        Bitmap selectionImg = BitmapFactory.decodeStream(inputStream);
                        ivProduct_addProduct.setImageBitmap(selectionImg);
                        uriImageSelectionOnDevice = uriImage;
                    } catch (FileNotFoundException e) {
                        System.out.println("Lỗi đọc đường dẫn đến hình ảnh: " + e.getMessage());
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Đây không phải là hình ảnh. Vui lòng chọn lại");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }
    }

    //    hàm ẩn bàn phím
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //    hàm lấy danh sách danh mục sản phẩm -----------------Category
    private void getCategory() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot category : snapshot.getChildren()) {
                        String ketCategoryItem = category.getKey();
                        String idCategoryChildItem = category.child("idCategory").getValue().toString();
                        String nameCategoryChildItem = category.child("nameCategory").getValue().toString();
                        Category categoryItem = new Category(ketCategoryItem, idCategoryChildItem, nameCategoryChildItem);
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
    private void getManuface() {
        arrManuface.clear();
        Manuface manuface = new Manuface(null, null, "Vui lòng chọn hãng sản xuất", null);
        arrManuface.add(manuface);
        manufaceSpinerAdapter.notifyDataSetChanged();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Manuface");
        Query databaseReferenceQuey = databaseReference.orderByChild("keyManuface_Category").equalTo(categorySelection.getKeyCategoryItem());
        databaseReferenceQuey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot manuface_Category : snapshot.getChildren()) {
                    String keyManufaceItem = manuface_Category.getKey();
                    String idManuface = manuface_Category.child("idManuface").getValue().toString();
                    String nameManuface = manuface_Category.child("nameManuface").getValue().toString();
                    String keyManuface_Category = manuface_Category.child("keyManuface_Category").getValue().toString();
                    Manuface manuface = new Manuface(keyManufaceItem, idManuface, nameManuface, keyManuface_Category);
                    arrManuface.add(manuface);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}