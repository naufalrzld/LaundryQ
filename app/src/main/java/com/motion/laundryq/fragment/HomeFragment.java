package com.motion.laundryq.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq.R;
import com.motion.laundryq.adapter.LaundryAdapter;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.LaundryModel;
import com.motion.laundryq.model.TimeOperationalModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY_NAME;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY_PRICE;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY_UNIT;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_DELIVERY_ORDER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_LAUNDRY;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_LAUNDRY_SERVICES;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_TIME_CLOSE;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_TIME_OPEN;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_TIME_OPERATIONAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.lyt_filter)
    LinearLayout lytFilter;
    @BindView(R.id.lyt_sort)
    LinearLayout lytSort;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private DatabaseReference dbLaundryRef;
    private DatabaseReference dbLaundryServicesRef;
    private DatabaseReference dbCategoryRef;
    private LaundryAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        dbLaundryRef= FirebaseDatabase.getInstance().getReference(FDB_KEY_LAUNDRY);
        dbLaundryServicesRef= FirebaseDatabase.getInstance().getReference(FDB_KEY_LAUNDRY_SERVICES);
        dbCategoryRef= FirebaseDatabase.getInstance().getReference(FDB_KEY_CATEGORY);

        adapter = new LaundryAdapter(getContext());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvList.setAdapter(adapter);

        lytFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterMenu(view);
            }
        });

        lytSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortMenu(view);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLaundry();
            }
        });

        getLaundry();

        return v;
    }

    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_filter, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuFilterItemClickListener());
        popup.show();
    }

    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sort, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuSortItemClickListener());
        popup.show();
    }

    private class MenuFilterItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MenuFilterItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_filter_pakaian:
                    Toast.makeText(getContext(), "Pakaian", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_filter_sepatu:
                    Toast.makeText(getContext(), "Sepatu", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_filter_jas:
                    Toast.makeText(getContext(), "Jas", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_filter_boneka:
                    Toast.makeText(getContext(), "Boneka", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_filter_bed_cover:
                    Toast.makeText(getContext(), "Bed Cover", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    }

    private class MenuSortItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MenuSortItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_sort_terdekat:
                    Toast.makeText(getContext(), "Terdekat", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_sort_termurah:
                    Toast.makeText(getContext(), "Harga Terendah", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_sort_termahal:
                    Toast.makeText(getContext(), "Harga Tertinggi", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    }

    private void getLaundry() {
        swipeRefreshLayout.setRefreshing(true);
        dbLaundryServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<LaundryModel> laundryList = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    final List<CategoryModel> categoryList = new ArrayList<>();
                    final List<TimeOperationalModel> timeList = new ArrayList<>();
                    String laundryID = ds.getKey();

                    final Boolean deliveryOrder = ds.child(FDB_KEY_DELIVERY_ORDER).getValue(Boolean.class);

                    assert laundryID != null;
                    dbLaundryRef.child(laundryID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            swipeRefreshLayout.setRefreshing(false);
                            LaundryModel laundryModel = dataSnapshot.getValue(LaundryModel.class);

                            assert laundryModel != null;
                            for (DataSnapshot categories : ds.child(FDB_KEY_CATEGORIES).getChildren()) {
                                final String categoryID = categories.getKey();
                                final Integer categoryPrice = categories.child(FDB_KEY_CATEGORY_PRICE).getValue(Integer.class);
                                final String categoryUnit = categories.child(FDB_KEY_CATEGORY_UNIT).getValue(String.class);

                                assert categoryID != null;
                                dbCategoryRef.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String categoryName = dataSnapshot.child(FDB_KEY_CATEGORY_NAME).getValue(String.class);

                                        CategoryModel categoryModel = new CategoryModel();
                                        categoryModel.setCategoryID(categoryID);
                                        categoryModel.setCategoryName(categoryName);
                                        categoryModel.setCategoryUnit(categoryUnit);
                                        categoryModel.setCategoryPrice(categoryPrice);

                                        categoryList.add(categoryModel);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("error", "onCancelled: " + databaseError.getMessage());
                                    }
                                });
                            }

                            for (DataSnapshot timeOperational : ds.child(FDB_KEY_TIME_OPERATIONAL).getChildren()) {
                                String day = timeOperational.getKey();
                                String timeOpen = timeOperational.child(FDB_KEY_TIME_OPEN).getValue(String.class);
                                String timeClose = timeOperational.child(FDB_KEY_TIME_CLOSE).getValue(String.class);

                                timeList.add(new TimeOperationalModel(day, timeOpen, timeClose));
                            }

                            laundryModel.setDeliveryOder(deliveryOrder);
                            laundryModel.setCategories(categoryList);
                            laundryModel.setTimeOperational(timeList);

                            laundryList.add(laundryModel);

                            adapter.setData(laundryList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("error", "onCancelled: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
