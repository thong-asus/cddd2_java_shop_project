package com.example.duancuahang.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShowMessage;
import com.example.duancuahang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateNameProductDialog extends DialogFragment {
    private ProductData productData = new ProductData();
    private UpdateNameProductDialog.ChangeNameProduct changeNameProduct;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    EditText edtNameProduct;
    private View view;

    public UpdateNameProductDialog(ProductData productData,UpdateNameProductDialog.ChangeNameProduct changeNameProduct){
        this.productData = productData;
        this.changeNameProduct = changeNameProduct;
    }
    public  interface ChangeNameProduct{
        public void getNameProductNew(String nameProductNew);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.TransparentDialog);
        view = LayoutInflater.from(getContext()).inflate(R.layout.customer_dialog_updatenameproduct,null);
        edtNameProduct = view.findViewById(R.id.edtNameProductNew_Customer_dialogUpdateNameProducyt);
        edtNameProduct.setText(productData.getNameProduct());
        builder.setNegativeButton("lưu thay đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String edtValue = edtNameProduct.getText().toString();
                if (!edtValue.isEmpty() && edtValue != null){
                    productData.setNameProduct(edtValue);
                    databaseReference = firebaseDatabase.getReference("Product/"+productData.getIdUserProduct() +"/"+productData.getIdProduct());
                    databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            changeNameProduct.getNameProductNew(edtValue);
                            dialogInterface.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Sửa tên sản phẩm không thành công. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Tên sản phẩm không hợp lệ. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width); // Sử dụng giá trị kích thước từ resources
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height_updatenameproduct);
        getDialog().getWindow().setLayout(width,height);
    }
}