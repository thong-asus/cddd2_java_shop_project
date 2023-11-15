package com.example.duancuahang.RecyclerView;

import static com.example.duancuahang.Class.ShowMessage.context;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.CommentProduct;
import com.example.duancuahang.Class.Customer;
import com.example.duancuahang.Class.LikeProduct;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewCommentedDetailItemAdapter extends RecyclerView.Adapter<ViewCommentedDetailItemViewHolder> {
    ArrayList<CommentProduct> arrCommentProduct;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ViewCommentedDetailItemAdapter(ArrayList<CommentProduct> arrCommentProduct, Context context) {
        this.arrCommentProduct = arrCommentProduct;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewCommentedDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewCommentedDetailItemViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_commented, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCommentedDetailItemViewHolder holder, int position) {
        CommentProduct commentProduct = arrCommentProduct.get(position);
        getAvatarCustomer(commentProduct.getIdCustomer(),holder);
        getCommentContent(commentProduct.getIdProduct(),holder);
    }
    private void getCommentContent(String idProduct, ViewCommentedDetailItemViewHolder holder) {
        //databaseReference = firebaseDatabase.getReference("CommentProduct/" + idProduct);
        databaseReference = firebaseDatabase.getReference("CommentProduct").child(idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    CommentProduct commentProduct = commentSnapshot.getValue(CommentProduct.class);
                    if (commentProduct != null) {
                        holder.tvDateCommented.setText(commentProduct.getDateComment());
                        holder.tvContentCommented.setText(commentProduct.getContentComment());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    //Lấy thông tin comment
//    private void getCommentContent(String idProduct, ViewCommentedDetailItemViewHolder holder) {
//        databaseReference = firebaseDatabase.getReference("CommentProduct/"+idProduct);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    CommentProduct commentProduct = snapshot.getValue(CommentProduct.class);
//                    //Picasso.get().load(customer.getImageUser()).placeholder(R.drawable.icondowload).into(holder.imgCustomerCommented);
//                    holder.tvDateCommented.setText(commentProduct.getDateComment());
//                    holder.tvContentCommented.setText(commentProduct.getContentComment());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    //Lấy hình ảnh và tên khách hàng
    private void getAvatarCustomer(String id, ViewCommentedDetailItemViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("Customer/"+id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    Picasso.get().load(customer.getImageUser()).placeholder(R.drawable.icondowload).into(holder.imgCustomerCommented);
                    holder.tvCustomerNameCommented.setText(customer.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        if (arrCommentProduct != null) {
            int size = arrCommentProduct.size();
            return size;
        }
        return 0;
    }
}
