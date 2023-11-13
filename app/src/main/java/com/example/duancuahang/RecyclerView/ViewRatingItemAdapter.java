package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewRatingItemAdapter extends RecyclerView.Adapter<ViewRatingItemViewHolder> {

    ArrayList<ProductData> productDataArrayList;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    public ViewRatingItemAdapter(ArrayList<ProductData> productDataArrayList, Context context) {
        this.productDataArrayList = productDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewRatingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewRatingItemViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_viewrating, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRatingItemViewHolder holder, int position) {
        ViewRatingItemAdapter adapter = new ViewRatingItemAdapter(productDataArrayList, context);
        ArrayList<ProductData> newData = new ArrayList<>();
        adapter.updateData(newData);

        if (productDataArrayList.size() > 0) {
            ProductData productData = productDataArrayList.get(position);
            //Lấy thông tin sản phẩm
            holder.tvProductNameItemRating.setText(productData.getNameProduct());
            getImageProduct(productData.getIdProduct(), holder);
            //Lấy số lượt thích của sản phẩm
            holder.tvSumLike.setText(productData.getSumLike()+"");
            //Lấy số lượt comment của sản phẩm
            holder.tvSumComment.setText(productData.getOverageCmtProduct()+"");

            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.drawable.bg_item01);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_item02);
            }
        }
    }


    //Lấy hình ảnh product
    private void getImageProduct(String idProduct, ViewRatingItemViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot imageItem : snapshot.getChildren()) {
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgItemViewRating);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateData(ArrayList<ProductData> newData) {
        if (newData != null) {
            productDataArrayList = new ArrayList<>(newData);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (productDataArrayList != null) {
            int size = productDataArrayList.size();
            return size;
        }
        return 0;
    }
}
