package com.motion.laundryq.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq.DetailOrderActivity;
import com.motion.laundryq.R;
import com.motion.laundryq.adapter.OrderAdapter;
import com.motion.laundryq.model.OrderLaundryModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_LAUNDRY;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_LAUNDRYID;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_ORDER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USERID;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ORDER_MODEL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_STATUS;
import static com.motion.laundryq.utils.AppConstant.KEY_INTENT_ORDER;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements OrderAdapter.OnCardViewClicked {
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;

    private List<OrderLaundryModel> orderList = new ArrayList<>();
    private OrderAdapter adapter;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, v);

        SharedPreference sharedPreference = new SharedPreference(getContext());
        UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);

        final String userID = userModel.getUserID();

        adapter = new OrderAdapter(getContext());
        adapter.setOnCardViewClicked(this);
        rvOrder.setHasFixedSize(true);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrder.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataOrder(userID);
            }
        });

        getDataOrder(userID);

        return v;
    }

    private void getDataOrder(String userID) {
        swipeRefreshLayout.setRefreshing(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FDB_KEY_ORDER);
        databaseReference.orderByChild(FDB_KEY_USERID).equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                swipeRefreshLayout.setRefreshing(false);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OrderLaundryModel orderLaundryModel = ds.getValue(OrderLaundryModel.class);
                    orderList.add(orderLaundryModel);
                }

                adapter.setOrderList(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onCardClick(OrderLaundryModel orderLaundryModel) {
        Intent intent = new Intent(getActivity(), DetailOrderActivity.class);
        intent.putExtra(KEY_DATA_INTENT_STATUS, KEY_INTENT_ORDER);
        intent.putExtra(KEY_DATA_INTENT_ORDER_MODEL, orderLaundryModel);
        startActivity(intent);
    }
}
