package com.example.duancuahang.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duancuahang.Class.Voucher;
import com.example.duancuahang.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VoucherUnavailableFragment extends Fragment {
    ArrayList<Voucher> voucherData = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    TextView tvNoVoucherUnavailable;
    RecyclerView rcvVoucherUnavailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voucher_unavailable, container, false);
        setControl(view);
        displayVoucher();

        return view;
    }

    private void displayVoucher() {
    }

    private void setControl(@NonNull View view) {
        rcvVoucherUnavailable = view.findViewById(R.id.rcvVoucherUnavailable);
        tvNoVoucherUnavailable = view.findViewById(R.id.tvNoVoucherUnavailable);
    }
}