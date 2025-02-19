package com.motion.laundryq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motion.laundryq.R;
import com.motion.laundryq.adapter.ViewPagerAdapter;
import com.motion.laundryq.fragment.order.CheckoutOrderFragment;
import com.motion.laundryq.fragment.order.DeliveryLocationFragment;
import com.motion.laundryq.fragment.order.PickLocationFragment;
import com.motion.laundryq.fragment.order.TimePickFragment;
import com.motion.laundryq.fragment.order.TypeLaundryFragment;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.OrderLaundryModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_ADMIN_COST;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_COST;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_ORDER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_ID;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.step)
    StateProgressBar step;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.btn_next)
    Button btnNext;

    private ViewPagerAdapter viewPagerAdapter;
    private OrderLaundryModel orderLaundryModel;

    private int viewPagerPosition, currentState;

    private Intent dataIntent;

    private ProgressDialog orderLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        orderLoading = new ProgressDialog(this);
        orderLoading.setMessage("Memesan . . .");
        orderLoading.setCancelable(false);

        SharedPreference sharedPreference = new SharedPreference(this);
        UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
        final String userID = userModel.getUserID();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderLaundryModel = new OrderLaundryModel();

        Locale locale = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", locale);
        Calendar calendar = Calendar.getInstance();
        final String orderID = sdf.format(calendar.getTime());

        setAdminCost();

        orderLaundryModel.setOrderID(orderID);

        dataIntent = getIntent();
        final String laundryID = dataIntent.getStringExtra(KEY_DATA_INTENT_LAUNDRY_ID);
        orderLaundryModel.setLaundryID(laundryID);

        setupViewPager(viewPager);

        viewPagerPosition = viewPager.getCurrentItem();

        setStepTitle((String) viewPagerAdapter.getPageTitle(viewPagerPosition));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    btnNext.setText("Order");
                } else {
                    btnNext.setText("Lanjut");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerPosition = viewPager.getCurrentItem();
                currentState = step.getCurrentStateNumber();
                if (viewPagerPosition != 4) {
                    Fragment fragment = viewPagerAdapter.getItem(viewPagerPosition);
                    if (fragment instanceof TypeLaundryFragment) {
                        TypeLaundryFragment tlf = (TypeLaundryFragment) fragment;

                        orderLaundryModel.setLaundryCost(tlf.getTotal());
                        orderLaundryModel.setCategories(tlf.getCategories());

                        nextViewPager(viewPagerPosition, currentState);
                    } else if (fragment instanceof PickLocationFragment) {
                        PickLocationFragment pcf = (PickLocationFragment) fragment;

                        if (pcf.isInputValid()) {
                            String address = pcf.getAddress();
                            String addressDetail = pcf.getAddressDetail();
                            double lat = pcf.getLatitude();
                            double lng = pcf.getLongitude();

                            AddressModel addressPickModel = new AddressModel(address, addressDetail, lat, lng);

                            orderLaundryModel.setAddressPick(addressPickModel);

                            nextViewPager(viewPagerPosition, currentState);
                        }
                    } else if (fragment instanceof DeliveryLocationFragment) {
                        DeliveryLocationFragment dlf = (DeliveryLocationFragment) fragment;
                        if (dlf.isInputValid()) {
                            String address = dlf.getAddress();
                            String addressDetail = dlf.getAddressDetail();
                            double lat = dlf.getLatitude();
                            double lng = dlf.getLongitude();

                            AddressModel addressDeliveryModel = new AddressModel(address, addressDetail, lat, lng);

                            orderLaundryModel.setAddressDelivery(addressDeliveryModel);

                            nextViewPager(viewPagerPosition, currentState);
                        }
                    } else if (fragment instanceof TimePickFragment) {
                        TimePickFragment tpf = (TimePickFragment) fragment;
                        String datePickup = tpf.getDatePickup();
                        String dateDelivery = tpf.getDateDelivery();
                        String timePickup = tpf.getTimePickup();
                        String timeDelivery = tpf.getTimeDelivery();

                        orderLaundryModel.setDatePickup(datePickup);
                        orderLaundryModel.setDateDelivery(dateDelivery);
                        orderLaundryModel.setTimePickup(timePickup);
                        orderLaundryModel.setTimeDelivery(timeDelivery);

                        CheckoutOrderFragment cof = (CheckoutOrderFragment) viewPagerAdapter.getItem(viewPagerPosition+1);
                        cof.setCategories(orderLaundryModel.getCategories());
                        cof.setLaundryCost(orderLaundryModel.getLaundryCost());
                        cof.setAdminCost(orderLaundryModel.getAdminCost());

                        cof.setTotal(orderLaundryModel.getLaundryCost() + orderLaundryModel.getAdminCost());

                        String addressPick = orderLaundryModel.getAddressPick().getAlamat();
                        if (!TextUtils.isEmpty(orderLaundryModel.getAddressPick().getAlamatDetail())) {
                            addressPick = orderLaundryModel.getAddressPick().getAlamatDetail() + " | " + orderLaundryModel.getAddressPick().getAlamat();
                        }

                        String addressDelivery = orderLaundryModel.getAddressDelivery().getAlamat();
                        if (!TextUtils.isEmpty(orderLaundryModel.getAddressDelivery().getAlamatDetail())) {
                            addressDelivery = orderLaundryModel.getAddressDelivery().getAlamatDetail() + " | " + orderLaundryModel.getAddressDelivery().getAlamat();
                        }

                        String dateTimePick = datePickup + ", " + timePickup;
                        String dateTimeDelivery = dateDelivery + ", " + timeDelivery;

                        cof.setOrderID(orderID);
                        cof.setAddressPickup(addressPick);
                        cof.setAddressDelivery(addressDelivery);
                        cof.setTimePickup(dateTimePick);
                        cof.setTimeDelivery(dateTimeDelivery);

                        orderLaundryModel.setDateOrder(cof.getDateOrder());

                        nextViewPager(viewPagerPosition, currentState);
                    }
                } else {
                    step.setAllStatesCompleted(true);

                    orderLaundryModel.setUserID(userID);
                    orderLaundryModel.setStatus(0);
                    orderLaundryModel.setLaundryID_status(laundryID + "_" + 0);
                    orderLaundryModel.setTotal(orderLaundryModel.getLaundryCost() + orderLaundryModel.getAdminCost());

                    saveOrder(orderLaundryModel);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();

        List<CategoryModel> categories = dataIntent.getParcelableArrayListExtra(KEY_DATA_INTENT_CATEGORIES);
        String laundryName = dataIntent.getStringExtra(KEY_DATA_INTENT_LAUNDRY_NAME);

        bundle.putParcelableArrayList(KEY_DATA_INTENT_CATEGORIES, (ArrayList<? extends Parcelable>) categories);
        bundle.putString(KEY_DATA_INTENT_LAUNDRY_NAME, laundryName);

        TypeLaundryFragment typeLaundryFragment = new TypeLaundryFragment();
        typeLaundryFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(typeLaundryFragment, "Jenis Cucian");
        viewPagerAdapter.addFragment(new PickLocationFragment(), "Lokasi Pengambilan");
        viewPagerAdapter.addFragment(new DeliveryLocationFragment(), "Lokasi Pengiriman");
        viewPagerAdapter.addFragment(new TimePickFragment(), "Waktu & Tanggal");
        viewPagerAdapter.addFragment(new CheckoutOrderFragment(), "Checkout");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setStepTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void nextViewPager(int position, int state) {
        switch (state) {
            case 1:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 2:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 3:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
            case 4:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                break;
        }
        setStepTitle((String) viewPagerAdapter.getPageTitle(position + 1));
        viewPager.setCurrentItem(position + 1);
        viewPagerPosition = viewPager.getCurrentItem();
    }

    private void previousViewPager(int position, int state) {
        switch (state) {
            case 2:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case 3:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 4:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 5:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
        }
        if (position != 0) {
            setStepTitle((String) viewPagerAdapter.getPageTitle(position - 1));
            viewPager.setCurrentItem(position - 1);
            viewPagerPosition = viewPager.getCurrentItem();
        } else {
            finish();
        }
    }

    private void saveOrder(OrderLaundryModel orderLaundryModel) {
        orderLoading.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FDB_KEY_ORDER);
        databaseReference.child(orderLaundryModel.getOrderID()).setValue(orderLaundryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderLoading.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(OrderActivity.this, "Order Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OrderActivity.this, "Order Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                currentState = step.getCurrentStateNumber();
                previousViewPager(viewPagerPosition, currentState);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdminCost() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FDB_KEY_COST);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer admin_cost = dataSnapshot.child(FDB_KEY_ADMIN_COST).getValue(Integer.class);
                orderLaundryModel.setAdminCost(admin_cost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
