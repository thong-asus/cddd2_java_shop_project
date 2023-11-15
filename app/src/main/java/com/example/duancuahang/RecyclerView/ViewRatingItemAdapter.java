package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.Image;
import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.Fragment.ViewLikeProductFragment;
import com.example.duancuahang.OrderDetailActivity;
import com.example.duancuahang.R;
import com.example.duancuahang.ViewRatingDetailActivity;
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
    private ProductData productData;


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

            final int finalPosition = position;
            holder.linearLayout_ItemViewRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewRatingDetailActivity.class);
                    /////////Truyền dữ liệu qua màn hình ViewRating detail///////////
                    ProductData productData1 = productDataArrayList.get(finalPosition);
                    intent.putExtra("productData1", productData1);
                    System.out.println("Dữ liệu PRODUCT DATA truyền đi tại VIEW RATING ITEM: "+productData1);
                    ////////////////////////////////////////////////////////////
                    context.startActivity(intent);
                }
            });
        }
    }
//    public static ViewLikeProductFragment newInstance(ProductData productData) {
//        ViewLikeProductFragment fragment = new ViewLikeProductFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("productData", productData);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
