package com.example.duancuahang.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.duancuahang.Fragment.ProductisinofstockFragment;
import com.example.duancuahang.Fragment.ProductisoutofstockFragment;

public class ProductListViewPagerAdapter extends FragmentStatePagerAdapter {

    public ProductListViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProductisinofstockFragment();
            case 1:
                return new ProductisoutofstockFragment();
            default:
                return new ProductisinofstockFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Còn hàng";
                break;
            case 1:
                title = "Hết hàng";
                break;
        }
        return title;
    }
}
