package com.example.duancuahang.RecyclerView;

import android.database.DataSetObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.duancuahang.Fragment.OrderCancelledFragment;
import com.example.duancuahang.Fragment.OrderDeliveredFragment;
import com.example.duancuahang.Fragment.OrderDeliveringFragment;
import com.example.duancuahang.Fragment.OrderWaitForConfirmFragment;
import com.example.duancuahang.Fragment.OrderWaitForTakeGoodsFragment;

public class Order_ListViewPagerAdapter extends FragmentStatePagerAdapter {

    public Order_ListViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrderWaitForConfirmFragment();
            case 1:
                return new OrderWaitForTakeGoodsFragment();
            case 2:
                return new OrderDeliveringFragment();
            case 3:
                return new OrderDeliveredFragment();
            case 4:
                return new OrderCancelledFragment();
        }
        return new OrderWaitForConfirmFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chờ lấy hàng";
                break;
            case 2:
                title = "Đang giao";
                break;
            case 3:
                title = "Đã giao";
                break;
            case 4:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
