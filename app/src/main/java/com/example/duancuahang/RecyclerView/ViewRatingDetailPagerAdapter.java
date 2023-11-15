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
import com.example.duancuahang.Fragment.ViewCommentProductFragment;
import com.example.duancuahang.Fragment.ViewLikeProductFragment;

public class ViewRatingDetailPagerAdapter extends FragmentStatePagerAdapter {
    private String product;
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ViewRatingDetailPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ViewLikeProductFragment(getProduct());
            case 1:
                return new ViewCommentProductFragment(getProduct());
        }
        return new ViewLikeProductFragment(getProduct());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Yêu thích";
                break;
            case 1:
                title = "Bình luận";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
