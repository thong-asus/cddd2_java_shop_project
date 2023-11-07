package com.example.duancuahang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.duancuahang.Class.ImageProduct;
import com.example.duancuahang.RecyclerView.ListImageProductViewHolder;
import com.example.duancuahang.RecyclerView.ListImageProduct_Adapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import kotlin._Assertions;

public class AddproductMulp extends AppCompatActivity {

    RecyclerView rcvImageProduct_addProductMulp;
    EditText edtQuanlityImageProduct;
    Button btnAddImageProduct;
    ImageView ivProductLage_AddProductMulp;
    Context context;
    ArrayList<ImageProduct> imgaProductArrayList = new ArrayList<>();
    ListImageProduct_Adapter listImageProductAdapter;

    private int vitriImageSelection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct_mulp);
        context = this;
        setControl();
        setIntiazation();
        setEvent();
    }

    //    hàm khởi tạo
    private void setIntiazation() {
        ImageProduct imageProduct = new ImageProduct();
        imgaProductArrayList.add(imageProduct);
        listImageProductAdapter = new ListImageProduct_Adapter(imgaProductArrayList, context);
        rcvImageProduct_addProductMulp.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rcvImageProduct_addProductMulp.setAdapter(listImageProductAdapter);
        listImageProductAdapter.notifyDataSetChanged();
    }

    // hàm bắt sự kiện
    private void setEvent() {
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
                    ivProductLage_AddProductMulp.setImageResource(R.drawable.icondowload);
                } else {
                    InputStream inputStream = null;
                    try {
                        inputStream = context.getContentResolver().openInputStream(imageProduct.getUrlImage());
                        Bitmap selectionImg = BitmapFactory.decodeStream(inputStream);
                        ivProductLage_AddProductMulp.setImageBitmap(selectionImg);
                    } catch (FileNotFoundException e) {
                        System.out.println("Loi lay hinh anh trong OpenInputStream: " + e.getMessage());
                    }

                }
            }
        });

//        hàm bắt sự kiện nhấn vào ảnh lớn
        ivProductLage_AddProductMulp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(vitriImageSelection < 0){
                   vitriImageSelection = 0;
               }
               moveLibraryImage();
            }
        });
    }

    //    hàm ánh xạ
    private void setControl() {
        rcvImageProduct_addProductMulp = findViewById(R.id.rcvImageProduct_addProductMulp);
        edtQuanlityImageProduct = findViewById(R.id.edtQuanlityImageProduct);
        btnAddImageProduct = findViewById(R.id.btnAddImageProduct);
        ivProductLage_AddProductMulp = findViewById(R.id.ivProductLage_AddProductMulp);
    }

    //    hàm mở thư viện ảnh
    private void moveLibraryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    //    hàm đưa hình ảnh lên
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
                        ivProductLage_AddProductMulp.setImageBitmap(selectionImg);
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
}