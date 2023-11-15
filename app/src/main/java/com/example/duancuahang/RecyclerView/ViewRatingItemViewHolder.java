package com.example.duancuahang.RecyclerView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duancuahang.R;

public class ViewRatingItemViewHolder extends RecyclerView.ViewHolder {
    ImageView imgItemViewRating;
    ProgressBar progessBar_loadingItemViewRating;
    TextView tvProductNameItemRating, tvSumLike, tvSumComment;
    LinearLayout linearLayout_ItemViewRating;

    public ViewRatingItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imgItemViewRating = itemView.findViewById(R.id.imgItemViewRating);
        progessBar_loadingItemViewRating = itemView.findViewById(R.id.progessBar_loadingItemViewRating);
        tvProductNameItemRating = itemView.findViewById(R.id.tvProductNameItemRating);
        tvSumLike = itemView.findViewById(R.id.tvSumLike);
        tvSumComment = itemView.findViewById(R.id.tvSumComment);
        linearLayout_ItemViewRating = itemView.findViewById(R.id.linearLayout_ItemViewRating);
    }
}
