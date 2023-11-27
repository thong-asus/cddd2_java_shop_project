package com.example.duancuahang.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDescriptionProduct extends DialogFragment {
    private ProductData productData= new ProductData();
    private View view;
    private UpdateDescriptionProduct.ChangeDescriptionProduct changeDescriptionProduct;

    DatabaseReference databaseReference ;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    EditText edtDescriptionProduct;
    public UpdateDescriptionProduct(ProductData productData,UpdateDescriptionProduct.ChangeDescriptionProduct changeDescriptionProduct){
        this.productData = productData;
        this.changeDescriptionProduct = changeDescriptionProduct;
    }
    public interface ChangeDescriptionProduct{
        public void getDescriptionChange(String description);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.TransparentDialog);
        view = LayoutInflater.from(getContext()).inflate(R.layout.customer_dialog_updatedescriptionproduct,null);
        edtDescriptionProduct = view.findViewById(R.id.edtDescriptitonProductNew_CustomerDialogUpdateNameProducyt);
        edtDescriptionProduct.setText(productData.getDescriptionProduct());
        builder.setNegativeButton("Lưu thay đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String edtDescript = edtDescriptionProduct.getText().toString();
                if (!edtDescript.isEmpty() && edtDescript != null){
                    productData.setDescriptionProduct(edtDescript);
                    databaseReference = firebaseDatabase.getReference("Product/"+productData.getIdUserProduct() +"/"+productData.getIdProduct());
                    databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            changeDescriptionProduct.getDescriptionChange(edtDescript);
                            dialogInterface.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Lỗi mô tả sản phẩm. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Mô tả sản phẩm không hợp lê. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
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
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height_updateDescriptionproduct);
        getDialog().getWindow().setLayout(width,height);
    }
}
