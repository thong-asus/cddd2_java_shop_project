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

public class UpdateQuanlityProductDialog extends DialogFragment {
    private ProductData productData = new ProductData();
    private View view;
    EditText edtQuanlityProduct;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    UpdateQuanlityProductDialog.ChangeQuanlityProduct changeQuanlityProduct;
    
    public  interface ChangeQuanlityProduct{
        public void getQuanlityProductNew(int quanlity);
    }
    
    public UpdateQuanlityProductDialog(ProductData productData, UpdateQuanlityProductDialog.ChangeQuanlityProduct changeQuanlityProduct){
        this.productData = productData;
        this.changeQuanlityProduct = changeQuanlityProduct;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.TransparentDialog);
        view = LayoutInflater.from(getContext()).inflate(R.layout.customer_dialog_updatequanlityproduct,null);
        edtQuanlityProduct = view.findViewById(R.id.edtQuanlityProductNew_Customer_dialogUpdateProduct);
        edtQuanlityProduct.setText(productData.getQuanlityProduct()+"");
        builder.setNegativeButton("Lưu thay đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int quanlity = Integer.parseInt(edtQuanlityProduct.getText().toString());
                productData.setQuanlityProduct(quanlity);
               databaseReference = firebaseDatabase.getReference("Product/"+productData.getIdUserProduct()+"/"+productData.getIdProduct());
               databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       changeQuanlityProduct.getQuanlityProductNew(quanlity);
                       dialogInterface.dismiss();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getContext(), "Sửa số lượng sản phẩm thất bại. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                   }
               });
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
