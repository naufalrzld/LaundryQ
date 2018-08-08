package com.motion.laundryq.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_ACTIVE;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_CATEGORY_ICON;
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
public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
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

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;

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
                getLaundry1();
            }
        });

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        getLaundry1();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
        Log.d("onDestroy", "onDestroy: TRUE");
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermission();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            double latitude = mylocation.getLatitude();
            double longitude = mylocation.getLongitude();

            adapter.setCurrentLocation(new LatLng(latitude, longitude));
            adapter.notifyDataSetChanged();
        }
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

    private void getLaundry1() {
        swipeRefreshLayout.setRefreshing(true);
        dbLaundryRef.orderByChild(FDB_KEY_ACTIVE).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final List<LaundryModel> laundryList = new ArrayList<>();
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        final LaundryModel laundryModel = ds.getValue(LaundryModel.class);
                        assert laundryModel != null;
                        String laundryID = laundryModel.getLaundryID();

                        dbLaundryServicesRef.child(laundryID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                swipeRefreshLayout.setRefreshing(false);
                                final List<CategoryModel> categoryList = new ArrayList<>();
                                final List<TimeOperationalModel> timeList = new ArrayList<>();

                                final Boolean deliveryOrder = dataSnapshot.child(FDB_KEY_DELIVERY_ORDER).getValue(Boolean.class);

                                for (DataSnapshot categories : dataSnapshot.child(FDB_KEY_CATEGORIES).getChildren()) {
                                    final String categoryID = categories.getKey();
                                    final CategoryModel categoryModel = categories.getValue(CategoryModel.class);
                                    assert categoryModel != null;
                                    categoryModel.setCategoryID(categoryID);

                                    assert categoryID != null;
                                    dbCategoryRef.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String icon = dataSnapshot.child(FDB_KEY_CATEGORY_ICON).getValue(String.class);

                                            categoryModel.setIcon(icon);

                                            categoryList.add(categoryModel);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("error", "onCancelled: " + databaseError.getMessage());
                                        }
                                    });
                                }

                                for (DataSnapshot timeOperational : dataSnapshot.child(FDB_KEY_TIME_OPERATIONAL).getChildren()) {
                                    String day = timeOperational.getKey();
                                    TimeOperationalModel tom = timeOperational.getValue(TimeOperationalModel.class);
                                    assert tom != null;
                                    tom.setDay(day);

                                    timeList.add(tom);
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
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
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
                            for (final DataSnapshot categories : ds.child(FDB_KEY_CATEGORIES).getChildren()) {
                                final String categoryID = categories.getKey();
                                final Integer categoryPrice = categories.child(FDB_KEY_CATEGORY_PRICE).getValue(Integer.class);
                                final String categoryUnit = categories.child(FDB_KEY_CATEGORY_UNIT).getValue(String.class);

                                assert categoryID != null;
                                dbCategoryRef.child(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String categoryName = dataSnapshot.child(FDB_KEY_CATEGORY_NAME).getValue(String.class);
                                        String icon = dataSnapshot.child(FDB_KEY_CATEGORY_ICON).getValue(String.class);

                                        CategoryModel categoryModel = new CategoryModel();
                                        categoryModel.setCategoryID(categoryID);
                                        categoryModel.setCategoryName(categoryName);
                                        categoryModel.setCategoryUnit(categoryUnit);
                                        categoryModel.setCategoryPrice(categoryPrice);
                                        categoryModel.setIcon(icon);

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

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(@NonNull Result result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(getActivity(),
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(getActivity(),
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }
}
