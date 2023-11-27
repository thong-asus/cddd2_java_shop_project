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

import java.text.NumberFormat;
import java.util.Locale;

public class UpdatePriceProductDialog extends DialogFragment {
    private ProductData productData = new ProductData();
    private View view;
    private UpdatePriceProductDialog.ChangePriceProduct changePriceProduct;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    EditText edtPrieProductNew;

    public UpdatePriceProductDialog(ProductData productData, UpdatePriceProductDialog.ChangePriceProduct changePriceProduct) {
        this.productData = productData;
        this.changePriceProduct = changePriceProduct;
    }

    public interface ChangePriceProduct {
        public void getPriceProductNew(int price);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.TransparentDialog);
        view = LayoutInflater.from(getContext()).inflate(R.layout.customer_dialog_updatepriceproduct, null);
        edtPrieProductNew = view.findViewById(R.id.edtPriceProductNew_Customer_dialogUpdateProduct);
        edtPrieProductNew.setText(productData.getPriceProduct()+"");
        builder.setNegativeButton("Lưu thay đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String edtPriceNew = edtPrieProductNew.getText().toString();
                if (!edtPriceNew.isEmpty() && edtPriceNew != null) {
                    productData.setPriceProduct(Integer.parseInt(edtPriceNew));
                    databaseReference = firebaseDatabase.getReference("Product/" + productData.getIdUserProduct() + "/" + productData.getIdProduct());
                    databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            changePriceProduct.getPriceProductNew(Integer.parseInt(edtPriceNew));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Lỗi khi sửa giá sản phẩm. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Giá không hợp lệ. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
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
