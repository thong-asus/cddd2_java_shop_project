package com.example.duancuahang.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.ProductData;
import com.example.duancuahang.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductStatisticsDialog extends DialogFragment {
    private ProductData productData;
    BarChart barChart;
    private View view;
    int monthStatistics = 1;
    ArrayList<BarEntry> arrBarEntry = new ArrayList<>();

    public ProductStatisticsDialog(ProductData productData) {
        this.productData = productData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.customer_productstatistics_dialogframent, null);
        barChart = view.findViewById(R.id.barchart);
        getStatisticsPRoduct();
        builder.setView(view);
        return builder.create();
    }
    private void getStatisticsPRoduct(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OrderProduct/");
            Query query = databaseReference.orderByChild("idProduct_Order").equalTo(productData.getIdProduct());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //System.out.println("thang: " + monthStatistics);
                    int count = 0;
                    if (snapshot.exists()){
                        for (DataSnapshot item:
                             snapshot.getChildren()) {
                            OrderData orderData = item.getValue(OrderData.class);
                            String moth = orderData.getDateOrder().split("/")[1];
                            System.out.println("motn: " + moth);
                            if (Integer.parseInt(moth) == monthStatistics){
                                count ++;
                            }
                        }
                    }
                    arrBarEntry.add(new BarEntry(monthStatistics,(Integer)count));
                    System.out.println("size: " + arrBarEntry.size());
                    if (arrBarEntry.size() >= 12){
                        BarDataSet barDataSet = new BarDataSet(arrBarEntry,"Bán được");
                        BarData barData = new BarData(barDataSet);
                        // Cấu hình trục y để bắt đầu từ giá trị 0
                        YAxis leftAxis = barChart.getAxisLeft();
                        leftAxis.setAxisMinimum(0f);
                        barChart.setPinchZoom(true); // Cho phép zoom bằng cử chỉ pinch
                        barChart.setDragEnabled(true);
                        barChart.setData(barData);
                        barChart.getDescription().setEnabled(false);
                        barChart.invalidate();
                    }
                    else {
                        monthStatistics ++;
                        getStatisticsPRoduct();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    @Override
    public void onStart() {
        super.onStart();
            int width = getResources().getDimensionPixelSize(R.dimen.dialog_width); // Sử dụng giá trị kích thước từ resources
            int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            getDialog().getWindow().setLayout(width,height);
    }
}
