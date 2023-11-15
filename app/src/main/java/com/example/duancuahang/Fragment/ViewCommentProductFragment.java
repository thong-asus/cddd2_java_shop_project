package com.example.duancuahang.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duancuahang.Class.CommentProduct;
import com.example.duancuahang.Class.LikeProduct;
import com.example.duancuahang.R;
import com.example.duancuahang.RecyclerView.ViewCommentedDetailItemAdapter;
import com.example.duancuahang.RecyclerView.ViewLikedDetailItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ViewCommentProductFragment extends Fragment {
    private RecyclerView rcvCommented;
    View vFragmentViewComment;
    TextView tvNoCommented;
    private ViewCommentedDetailItemAdapter viewCommentedDetailItemAdapter;
    private String idProduct = "";
    ArrayList<CommentProduct> arrCommentProduct = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public ViewCommentProductFragment(String idProduct){
        this.idProduct = idProduct;
        //this.idCustomer = idCustomer;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_comment_product, container, false);

        Bundle args = getArguments();
        if (args != null) {
            idProduct = args.getString("idProduct", "");
        }
        setControl(view);
        setInitialization();
        //displayCommentsForProduct(idProduct);
        displayAllComment();
        return view;
    }
    private void displayCommentsForProduct(String idProduct) {
        databaseReference = firebaseDatabase.getReference("CommentProduct").child(idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cmtItem : snapshot.getChildren()) {
                    CommentProduct cmtProduct = cmtItem.getValue(CommentProduct.class);
                    if (cmtProduct != null) {
                        arrCommentProduct.add(cmtProduct);
                    }
                }
                viewCommentedDetailItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private void displayAllComment() {
        databaseReference = firebaseDatabase.getReference("CommentProduct");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    // Lấy giá trị của comment dưới dạng Map
                    Map<String, Object> commentMap = (Map<String, Object>) commentSnapshot.getValue();

                    // Kiểm tra null và đảm bảo có đủ các trường thông tin
                    if (commentMap != null && commentMap.containsKey("contentComment")
                            && commentMap.containsKey("dateComment") && commentMap.containsKey("idCustomer")
                            && commentMap.containsKey("idProduct")) {

                        // Tạo đối tượng CommentProduct từ Map
                        CommentProduct commentProduct = new CommentProduct();
                        commentProduct.setContentComment((String) commentMap.get("contentComment"));
                        commentProduct.setDateComment((String) commentMap.get("dateComment"));
                        commentProduct.setIdCustomer((String) commentMap.get("idCustomer"));
                        commentProduct.setIdProduct((String) commentMap.get("idProduct"));

                        // Kiểm tra idProduct để xác định có thêm vào danh sách không
                        if (commentProduct.getIdProduct().equals(idProduct)) {
                            arrCommentProduct.add(commentProduct);
                            viewCommentedDetailItemAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }


    //        private void displayAllComment() {
//        databaseReference = firebaseDatabase.getReference("CommentProduct");
//            //Query query = databaseReference.orderByKey();
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot itemCustomer:
//                        snapshot.getChildren()) {
//                    for (DataSnapshot cmtItem:
//                            itemCustomer.getChildren()) {
//                        CommentProduct cmtProduct = cmtItem.getValue(CommentProduct.class);
//                        if(cmtProduct.getIdProduct().equals(idProduct)){
//                            arrCommentProduct.add(cmtProduct);
//                            viewCommentedDetailItemAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    private void setInitialization() {
        // Khởi tạo adapter
        viewCommentedDetailItemAdapter = new ViewCommentedDetailItemAdapter(arrCommentProduct, getContext());
        rcvCommented.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCommented.setAdapter(viewCommentedDetailItemAdapter);
    }
    private void setControl(@NonNull View view) {
        rcvCommented = view.findViewById(R.id.rcvCommented);
        tvNoCommented = view.findViewById(R.id.tvNoCommented);
        vFragmentViewComment = view.findViewById(R.id.vFragmentViewComment);
    }
}