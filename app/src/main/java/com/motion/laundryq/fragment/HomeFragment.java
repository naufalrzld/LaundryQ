package com.motion.laundryq.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.motion.laundryq.R;
import com.motion.laundryq.adapter.LaundryAdapter;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.LaundryModel;
import com.motion.laundryq.utils.SpinnerItem;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.spn_filter)
    MaterialSpinner spnFilter;
    @BindView(R.id.spn_srot)
    MaterialSpinner spnSort;
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

        spnFilter.setItems(SpinnerItem.itemFilter());
        spnSort.setItems(SpinnerItem.itemSort());

        adapter = new LaundryAdapter(getContext());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvList.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLaundry();
            }
        });

        getLaundry();

        return v;
    }

    private void getLaundry() {
        swipeRefreshLayout.setRefreshing(true);
        dbLaundryServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<LaundryModel> laundryList = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    final List<CategoryModel> categoryList = new ArrayList<>();
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

                            laundryModel.setDeliveryOder(deliveryOrder);
                            laundryModel.setCategories(categoryList);

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

    /*private void getLaundry() {
        swipeRefreshLayout.setRefreshing(true);
        dbLaundryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                swipeRefreshLayout.setRefreshing(false);
                List<LaundryModel> laundryList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final LaundryModel laundryModel = data.getValue(LaundryModel.class);

                    assert laundryModel != null;
                    dbLaundryServicesRef.child(laundryModel.getLaundryID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean deliveryOrder = dataSnapshot.child(FDB_KEY_DELIVERY_ORDER).getValue(Boolean.class);
                            laundryModel.setDeliveryOder(deliveryOrder);

                            final List<CategoryModel> listCategory = new ArrayList<>();

                            for (DataSnapshot data : dataSnapshot.child(FDB_KEY_CATEGORIES).getChildren()) {
                                final CategoryModel categoryModel = new CategoryModel();

                                String categoryID = data.getKey();

                                categoryModel.setCategoryID(categoryID);

                                String categoryUnit = data.child(FDB_KEY_CATEGORY_UNIT).getValue(String.class);
                                Integer categoryPrice = data.child(FDB_KEY_CATEGORY_PRICE).getValue(Integer.class);

                                categoryModel.setCategoryUnit(categoryUnit);
                                categoryModel.setCategoryPrice(categoryPrice);

                                assert categoryID != null;
                                dbCategoryRef.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String categoryName = dataSnapshot.child(FDB_KEY_CATEGORY_NAME).getValue(String.class);
                                        categoryModel.setCategoryName(categoryName);

                                        listCategory.add(categoryModel);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("error", "onCancelled: " + databaseError.getMessage());
                                    }
                                });
                            }

                            laundryModel.setCategories(listCategory);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("error", "onCancelled: " + databaseError.getMessage());
                        }
                    });

                    laundryList.add(laundryModel);
                }

                adapter.setData(laundryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }*/
}
