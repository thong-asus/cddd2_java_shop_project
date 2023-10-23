package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duancuahang.Class.Product;
import com.example.duancuahang.R;

import java.util.List;

public class Product_InOfStockAdapter extends RecyclerView.Adapter<Product_InOfStockViewHolder> {

    List<Product> products;
    Context context;

    public Product_InOfStockAdapter(List<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public Product_InOfStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Product_InOfStockViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_itemproductlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Product_InOfStockViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvIdProductItem.setText(product.getIdProduct());
        holder.tvNameProductItem.setText(product.getNameProduct());
        holder.tvCategory.setText("Danh mục: "+String.valueOf(product.getCategoryProduct()));
        holder.tvManufaceProduct.setText("Hảng: "+String.valueOf(product.getManufaceProduct()));
        holder.tvPriceProduct.setText("Giá: "+String.valueOf(String.valueOf(product.getPriceProduct())) + "VND");
        holder.tvQuanlityProduct.setText("Số lượng: "+String.valueOf(product.getQuanlityProduct()));
        if (position % 2 == 0){
            holder.itemView.setBackgroundResource(R.drawable.bg_item01);
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.bg_item02);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
