package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.LikeProduct;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewLikedDetailItemAdapter extends RecyclerView.Adapter<ViewLikedDetailItemViewHolder> {
    ArrayList<LikeProduct> arrLikeProduct;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public ViewLikedDetailItemAdapter(ArrayList<LikeProduct> arrLikeProduct, Context context) {
        this.arrLikeProduct = arrLikeProduct;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewLikedDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewLikedDetailItemViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_person_liked, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewLikedDetailItemViewHolder holder, int position) {
        LikeProduct likeProduct = arrLikeProduct.get(position);
        getAvatarCustomer(likeProduct.getIdCustomer_LikeProduct(),holder);

    }
    //Lấy hình ảnh khách hàng
    private void getAvatarCustomer(String id, ViewLikedDetailItemViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("Customer/"+id);
        //Query query = databaseReference.orderByChild("id").equalTo(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    Picasso.get().load(customer.getImageUser()).placeholder(R.drawable.icondowload).into(holder.imgCustomerLiked);
                    holder.tvCustomerNameLiked.setText(customer.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        if (arrLikeProduct != null) {
            int size = arrLikeProduct.size();
            return size;
        }
        return 0;
    }
}
