package com.example.duancuahang.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.LikeProduct;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Class.ShopData;
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.ViewLikedDetailItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewLikeProductFragment extends Fragment {
    private RecyclerView rcvLiked;
    private TextView tvNoLiked;

    View vFragmentViewLike;
    private ViewLikedDetailItemAdapter viewLikedDetailItemAdapter;
    ProductData productData = new ProductData();
    private ShopData shopData = new ShopData();
    private String idProduct = "";
    private String idCustomer = "";
    ArrayList<Customer> customerArrayList = new ArrayList<>();
    ArrayList<LikeProduct> arrLikeProduct = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public ViewLikeProductFragment(String idProduct){
        this.idProduct = idProduct;
        this.idCustomer = idCustomer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_like_product, container, false);
        setControl(view);
        setInitialization();
        pullUserLikeProduct();
        return view;
    }

    private void setInitialization() {
        // Khởi tạo adapter
        viewLikedDetailItemAdapter = new ViewLikedDetailItemAdapter(arrLikeProduct, getContext());
        rcvLiked.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvLiked.setAdapter(viewLikedDetailItemAdapter);
    }

    private void pullUserLikeProduct() {
        databaseReference = firebaseDatabase.getReference("LikeProduct");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot itemCustome:
                    snapshot.getChildren()) {
                   for (DataSnapshot likeItem:
                        itemCustome.getChildren()) {
                        LikeProduct likeProduct = likeItem.getValue(LikeProduct.class);
                        if(likeProduct.getIdProduct_LikeProduct().equals(idProduct)){
                            arrLikeProduct.add(likeProduct);
                            viewLikedDetailItemAdapter.notifyDataSetChanged();
                        }
                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


    private void setControl(@NonNull View view) {
        rcvLiked = view.findViewById(R.id.rcvLiked);
        tvNoLiked = view.findViewById(R.id.tvNoLiked);
        vFragmentViewLike = view.findViewById(R.id.vFragmentViewLike);
    }
}