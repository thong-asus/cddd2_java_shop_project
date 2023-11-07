package com.example.duancuahang;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.ImageProduct;
import com.example.duancuahang.Class.Manuface;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.Class.Validates;
import com.example.duancuahang.RecyclerView.ListImageProductViewHolder;
import com.example.duancuahang.RecyclerView.ListImageProduct_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Addproduct extends AppCompatActivity {

    //    Biến giao diện
    Spinner spCategory, spManuface_AddProduct;
    LinearLayout vMain_AddProduct;
    ImageView ivProduct_addProduct;
    Button btnAddProduct;
    EditText edtNameProduct, edtDescriptionProduct, edtPriceProduct, edtQuanlityProduct;
    ProgressBar progressBar;
    Toolbar toolBar_AddProduct;
    NestedScrollView nestedScrollView_ScreenAddProduct;


    RecyclerView rcvImageProduct_addProduct;
    EditText edtQuanlityImageProduct;
    Button btnAddImageProduct;
    ArrayList<ImageProduct> imgaProductArrayList = new ArrayList<>();
    ListImageProduct_Adapter listImageProductAdapter;

    private int vitriImageSelection = -1;

    //    Biến xử lý
    private DatabaseReference databaseReference;
    private static final int    REQUEST_CODE_PICK_IMAGE = 1;
    private String urlImageSelection;
    private Category categorySelection = null;
    private Manuface manufaceSelection = null;
    private  boolean loading = true;
    private int countPushImg = 0;
    private  ArrayList<String> urlImgServe = new ArrayList<>();
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
        //Kích hoạt back button on toolbar
        setSupportActionBar(toolBar_AddProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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



        ImageProduct imageProduct = new ImageProduct();
        imgaProductArrayList.add(imageProduct);
        listImageProductAdapter = new ListImageProduct_Adapter(imgaProductArrayList, context);
        rcvImageProduct_addProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rcvImageProduct_addProduct.setAdapter(listImageProductAdapter);
        listImageProductAdapter.notifyDataSetChanged();
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

////        bắt sự kiện người dùng nhấn vào hình ảnh sản phẩm
//        ivProduct_addProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                moveLibraryImage();
//            }
//        });

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
                               hideKeyboard();
                               setEnableInComponentScreen();
                               loading = true;
                               progressBar.setVisibility(View.VISIBLE);
                               vMain_AddProduct.setBackgroundColor(Color.parseColor("#80000000"));
                               hideKeyboard();
                               pushImageProductToFirebaseStorage();
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
               // pushImageProductToFirebaseStorage();
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
//        sự kiện nhấn vào button thêm số lượng hình ảnh dựa vào số lượng vừa nhập vào
        btnAddImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtQuanlityImageProduct.getText().toString().length() < 1 || Integer.parseInt(edtQuanlityImageProduct.getText().toString()) < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Số lượng hình ảnh muốn thêm vào không hợp lệ");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    if (imgaProductArrayList.size() + Integer.parseInt(edtQuanlityImageProduct.getText().toString()) <= 5) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn chắc chắn muốn thêm " + edtQuanlityImageProduct.getText().toString() + " hình ảnh sản phẩm ?");
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int quanlityImageProduct = Integer.parseInt(edtQuanlityImageProduct.getText().toString());
                                for (int j = 0; j < Integer.parseInt(edtQuanlityImageProduct.getText().toString()); j++) {
                                    ImageProduct imageProduct = new ImageProduct();
                                    imgaProductArrayList.add(imageProduct);
                                }
                                listImageProductAdapter.notifyDataSetChanged();
                                edtQuanlityImageProduct.setText("");
                                hideKeyboard();
                            }
                        });
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Tổng số lượng hình ảnh đã vướt quá 5 hình. Thêm hình ảnh thất bại");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
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
        });

//        sự kiện nhấn vào item hình ảnh của sản phẩm
        listImageProductAdapter.setOnclickListener(new ListImageProduct_Adapter.OnclickListener() {
            @Override
            public void onItemClick(ListImageProductViewHolder listImageProductViewHolder, int position) {
                vitriImageSelection = position;
                ImageProduct imageProduct = imgaProductArrayList.get(position);
                if (imageProduct.getUrlImage() == null) {
                    ivProduct_addProduct.setImageResource(R.drawable.icondowload);
                } else {
                    InputStream inputStream = null;
                    try {
                        inputStream = context.getContentResolver().openInputStream(imageProduct.getUrlImage());
                        Bitmap selectionImg = BitmapFactory.decodeStream(inputStream);
                        ivProduct_addProduct.setImageBitmap(selectionImg);
                    } catch (FileNotFoundException e) {
                        System.out.println("Loi lay hinh anh trong OpenInputStream: " + e.getMessage());
                    }

                }
            }
        });

//        hàm bắt sự kiện nhấn vào ảnh lớn
        ivProduct_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vitriImageSelection < 0){
                    vitriImageSelection = 0;
                }
                moveLibraryImage();
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
        nestedScrollView_ScreenAddProduct = findViewById(R.id.nestedScrollView_ScreenAddProduct);
        rcvImageProduct_addProduct = findViewById(R.id.rcvImageProduct_addProduct);
        edtQuanlityImageProduct = findViewById(R.id.edtQuanlityImageProduct_AddProduct);
        btnAddImageProduct = findViewById(R.id.btnAddImageProduct_Addproduct);
    }

//    ngăn chặn thao tác người dùng
    private void setEnableInComponentScreen(){
        nestedScrollView_ScreenAddProduct.setEnabled(false);
        ivProduct_addProduct.setEnabled(false);
        edtNameProduct.setEnabled(false);
        edtDescriptionProduct.setEnabled(false);
        spCategory.setEnabled(false);
        spManuface_AddProduct.setEnabled(false);
        edtPriceProduct.setEnabled(false);
        edtQuanlityProduct.setEnabled(false);
        btnAddProduct.setEnabled(false);
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
        SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop",Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationShop","");
        Gson gson = new Gson();
       ShopData shopData = gson.fromJson(jsonShop, ShopData.class);
        String keyProductItem = getIdProductRan();
        ProductData productData = new ProductData(keyProductItem.concat(shopData.getIdShop()),
                edtNameProduct.getText().toString(),
                Integer.valueOf(edtPriceProduct.getText().toString()),
                categorySelection.getKeyCategoryItem(), manufaceSelection.getKeyManufaceItem(),
                Integer.valueOf(edtQuanlityProduct.getText().toString()),
                edtDescriptionProduct.getText().toString(), 0, 0,shopData.getIdShop());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Product").child(keyProductItem.concat(shopData.getIdShop())).setValue(productData);
        addImageProduct(keyProductItem.concat(shopData.getIdShop()));
        loading = false;
        System.out.println("Id shop add: "+ shopData.getIdShop());
        System.out.println("product add: "+ productData.toString());

       if (!loading){
           Intent intent = new Intent(context, Productlist.class);
           startActivity(intent);
           finish();
       }

    }

    private boolean checkValueProductBeforeAdd() {
        if (edtNameProduct.getText().toString() == null ||
                edtDescriptionProduct.getText().toString() == null ||
                edtPriceProduct.getText().toString() == null ||
                edtQuanlityProduct.getText().toString() == null ||
                categorySelection == null ||
                manufaceSelection == null|| imgaProductArrayList.get(0).getUrlImage() == null
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
    }

//    hàm tải hình ảnh lên firebase
    private void pushImageProductToFirebaseStorage(){
        for (int i = 0; i < imgaProductArrayList.size(); i++) {
            ImageProduct item = imgaProductArrayList.get(i);
            if (item.getUrlImage() != null){
                System.out.println("uri item: " + item.getUrlImage());
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imgRef = storageReference.child("imageProduct/"+item.getUrlImage().getLastPathSegment());
                UploadTask uploadTask = imgRef.putFile(item.getUrlImage());
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            countPushImg++;
                            System.out.println(countPushImg + "");
                            urlImageSelection = uri.toString();
                            urlImgServe.add(urlImageSelection);
                            if (countPushImg == imgaProductArrayList.size()){
                                System.out.println("xong");
                                AddProduct();
                            }
                        });
                    }
                });

            }
            else {
                imgaProductArrayList.remove(i);
            }
        }
    }

    private void addImageProduct(String idProduct){
        for (int i = 0; i < urlImgServe.size(); i++) {
            Image image = new Image(idProduct,urlImgServe.get(i));
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("ImageProducts");
            databaseReference.push().setValue(image);

        }
    }

    //    hàm hiển thị kết quả ảnh vừa chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 || resultCode == RESULT_OK) {
            Uri uriImage = data.getData();
            String mediaType = getContentResolver().getType(uriImage);
            if (mediaType != null) {
                if (mediaType.startsWith("image/")) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriImage);
                        Bitmap selectionImg = BitmapFactory.decodeStream(inputStream);
                        ivProduct_addProduct.setImageBitmap(selectionImg);
                        ImageProduct imageProduct = imgaProductArrayList.get(vitriImageSelection);
                        imageProduct.setUrlImage(uriImage);
                        listImageProductAdapter.notifyDataSetChanged();
                        vitriImageSelection = -1;
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