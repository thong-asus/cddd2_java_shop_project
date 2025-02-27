package com.example.duancuahang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duancuahang.Class.ActionToGetVoucher;
import com.example.duancuahang.R;

import java.util.ArrayList;

public class ActionToGetVoucherSpinnerAdapter extends BaseAdapter {
    private ArrayList<ActionToGetVoucher> arrActionToGetVoucher = new ArrayList<>();
    private Context context;

    public ActionToGetVoucherSpinnerAdapter(ArrayList<ActionToGetVoucher> arrActionToGetVoucher, Context context) {
        this.arrActionToGetVoucher = arrActionToGetVoucher;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrActionToGetVoucher.size();
    }

    @Override
    public Object getItem(int i) {
        return arrActionToGetVoucher.get(i);
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
        TextView tvNameCategoryItem = view.findViewById(R.id.tvNameCategory_SpinerCategory_Addproduct);
        tvNameCategoryItem.setText(arrActionToGetVoucher.get(i).getValueAction());
        return view;
    }
}
