package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.Class.ImageProduct;
import com.example.duancuahang.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListImageProduct_Adapter extends RecyclerView.Adapter<ListImageProductViewHolder> {
    ArrayList<ImageProduct> arrImageProduct = new ArrayList();
    Context context;
    private OnclickListener itemClick;

    public ListImageProduct_Adapter(ArrayList<ImageProduct> arrImageProduct, Context context) {
        this.arrImageProduct = arrImageProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ListImageProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListImageProductViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_imageproduct, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListImageProductViewHolder holder, int position) {
        ImageProduct imageProduct = arrImageProduct.get(position);
        if (imageProduct.getUrlImage() == null) {
            holder.ivImageItemProduct.setImageResource(R.drawable.icondowload);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(imageProduct.getUrlImage());
                Bitmap selectionImg = BitmapFactory.decodeStream(inputStream);
                holder.ivImageItemProduct.setImageBitmap(selectionImg);
            } catch (FileNotFoundException e) {
                System.out.println("Loi lay hinh anh trong OpenInputStream: " + e.getMessage());
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(holder,holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    public interface OnclickListener {
        void onItemClick(ListImageProductViewHolder listImageProductViewHolder, int position);
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        itemClick = onclickListener;
    }

    @Override
    public int getItemCount() {
        return arrImageProduct.size();
    }
}
