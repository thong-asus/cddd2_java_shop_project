package com.example.duancuahang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duancuahang.Class.Category;
import com.example.duancuahang.R;

import java.util.ArrayList;

public class CategorySpinerAdapter extends BaseAdapter {
    ArrayList<Category> categories = new ArrayList<>();
    Context context;

    public CategorySpinerAdapter(ArrayList<Category> categories, Context context){
        this.categories = categories;
        this.context = context;
    }
    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.customer_item_spinner_category,viewGroup,false);
        }
        LinearLayout vMainItemCategory = view.findViewById(R.id.vMainItemCategoryAdapter);
        if (i == 0){
            vMainItemCategory.setBackgroundResource(R.drawable.bg_item_spinner_item_introduce);
        }
//        else {
//           if (i % 2 == 0 && i != 0){
//               vMainItemCategory.setBackgroundResource(R.drawable.bg_item_spiner_custom_ietmselection01);
//           }
//           else {
//               vMainItemCategory.setBackgroundResource(R.drawable.bg_item_spinner_custom_itemselection_02);
//           }
//        }
        TextView tvNameCategoryItem = view.findViewById(R.id.tvNameCategory_SpinerCategory_Addproduct);
        tvNameCategoryItem.setText(categories.get(i).getNameCategory());
        return view;
    }
}
