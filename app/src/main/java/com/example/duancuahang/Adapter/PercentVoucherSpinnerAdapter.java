package com.example.duancuahang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duancuahang.Class.PercentDiscount;
import com.example.duancuahang.Class.Voucher;

import java.util.ArrayList;

public class PercentVoucherSpinnerAdapter extends BaseAdapter {
    ArrayList<PercentDiscount> arrPercentDiscount = new ArrayList<>();
    Context context;

    public PercentVoucherSpinnerAdapter(ArrayList<PercentDiscount> arrPercentDiscount, Context context) {
        this.arrPercentDiscount = arrPercentDiscount;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrPercentDiscount.size();
    }

    @Override
    public Object getItem(int i) {
        return arrPercentDiscount.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

       PercentDiscount percentDiscount = arrPercentDiscount.get(i);
        viewHolder.textView.setText(percentDiscount.getPercent() + "%");

        return view;
    }
    static class ViewHolder {
        TextView textView;
    }
}
