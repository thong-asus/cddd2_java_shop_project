package com.example.duancuahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.OrderData;
import com.example.duancuahang.Class.Validates;
import com.example.duancuahang.RecyclerView.OrderItem_Adapter;

import java.util.ArrayList;

public class SearchOrderActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView rcvOrderSearch;
    private TextView tvNoResultSearch;
    private Toolbar toolBar_SearchOrder;
    //OrderSearch_ListViewPagerAdapter orderSearchListViewPagerAdapter;
    Context context;
    private OrderItem_Adapter orderAdapter;
    private ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        setControl();
        setEvent();
        setInitialization();
        setSearchEvent();
        context = this;
    }


    private void setEvent() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Validates.validOrderSearch(edtSearch.getText().toString()) == false) {
                        edtSearch.selectAll();
                        edtSearch.requestFocus();
                        edtSearch.setError("Hãy nhập thông tin tìm kiếm");
                    }
                }
                return false;
            }
        });
    }
    private void setSearchEvent() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currentSearchString = charSequence.toString().trim();
                searchOrders(currentSearchString);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void searchOrders(String searchString) {
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        orderAdapter.setData(arrayOrderData);
        orderAdapter.notifyDataSetChanged();

        if (arrayOrderData.isEmpty()) {
            tvNoResultSearch.setVisibility(View.VISIBLE);
        } else {
            tvNoResultSearch.setVisibility(View.GONE);
        }
    }
    private void setInitialization() {
        orderAdapter = new OrderItem_Adapter(arrayOrderData, this);
        rcvOrderSearch.setLayoutManager(new LinearLayoutManager(this));
        rcvOrderSearch.setAdapter(orderAdapter);
//        orderSearchListViewPagerAdapter = new OrderSearch_ListViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
//        viewPager_SearchOrder.setAdapter(orderSearchListViewPagerAdapter);
        //Kích hoạt nút back
        setSupportActionBar(toolBar_SearchOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //Sự kiện nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setControl() {
        edtSearch = findViewById(R.id.edtSearch);
        toolBar_SearchOrder = findViewById(R.id.toolBar_SearchOrder);
        rcvOrderSearch = findViewById(R.id.rcvOrderSearch);
        tvNoResultSearch = findViewById(R.id.tvNoResultSearch);
    }
}