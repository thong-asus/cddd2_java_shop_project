package com.example.duancuahang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duancuahang.Class.Product;
import com.example.duancuahang.R;
import com.example.duancuahang.ViewHolder.ProductIs_InOfStockViewHolder;

import java.util.List;

public class ProductInOfStockAdapter extends RecyclerView.Adapter<ProductIs_InOfStockViewHolder> {

    List<Product> products;
    Context context;

    public ProductInOfStockAdapter(List<Product> products,Context context){
        this.products = products;
        this.context = context;
    }
    @NonNull
    @Override
    public ProductIs_InOfStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductIs_InOfStockViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_itemproductlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductIs_InOfStockViewHolder holder, int position) {
        Product product = products.get(position);
        holder.getTvIdProductItem().setText(product.getIdProduct());
        holder.getTvNameProductItem().setText(product.getNameProduct());
        holder.getTvCategory().setText(product.getCategoryProduct());
        holder.getTvManufaceProduct().setText(product.getManufaceProduct());
        holder.getTvPriceProduct().setText(product.getPriceProduct());
        holder.getTvQuanlityProduct().setText(product.getQuanlityProduct());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
