package com.example.duancuahang.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.duancuahang.Fragment.OrderCancelledFragment;
import com.example.duancuahang.Fragment.OrderDeliveredFragment;
import com.example.duancuahang.Fragment.OrderDeliveringFragment;
import com.example.duancuahang.Fragment.OrderWaitForConfirmFragment;
import com.example.duancuahang.Fragment.OrderWaitForTakeGoodsFragment;
import com.example.duancuahang.Fragment.VoucherAvailableFragment;
import com.example.duancuahang.Fragment.VoucherUnavailableFragment;

public class VoucherPagerAdapter extends FragmentStatePagerAdapter {

    public VoucherPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VoucherAvailableFragment();
            case 1:
                return new VoucherUnavailableFragment();
        }
        return new VoucherAvailableFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Đang hoạt động";
                break;
            case 1:
                title = "Kết thúc";
                break;
        }
        return title;
    }
}
