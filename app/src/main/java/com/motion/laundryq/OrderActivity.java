package com.motion.laundryq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motion.laundryq.adapter.ViewPagerAdapter;
import com.motion.laundryq.fragment.order.CheckoutOrderFragment;
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

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_ORDER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_ID;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;
import static com.motion.laundryq.utils.AppConstant.KEY_ORDER_LAUNDRY;
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

    private int viewPagerPosition, currentState;
    private String addressDelivery;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final OrderLaundryModel orderLaundryModel = new OrderLaundryModel();

        Locale locale = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", locale);
        Calendar calendar = Calendar.getInstance();
        final String orderID = sdf.format(calendar.getTime());

        orderLaundryModel.setOrderID(orderID);

        dataIntent = getIntent();
        String laundryID = dataIntent.getStringExtra(KEY_DATA_INTENT_LAUNDRY_ID);
        orderLaundryModel.setLaundryID(laundryID);

        SharedPreference sharedPreference = new SharedPreference(this);

        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            String userID = userModel.getUserID();
            orderLaundryModel.setUserID(userID);
            AddressModel addressModel = userModel.getAddress();
            addressDelivery = addressModel.getAlamatDetail() + " | " + addressModel.getAlamat();
        }

        setupViewPager(viewPager);

        viewPagerPosition = viewPager.getCurrentItem();

        setStepTitle((String) viewPagerAdapter.getPageTitle(viewPagerPosition));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
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
                if (viewPagerPosition != 3) {
                    Fragment fragment = viewPagerAdapter.getItem(viewPagerPosition);
                    if (fragment instanceof TypeLaundryFragment) {
                        TypeLaundryFragment tlf = (TypeLaundryFragment) fragment;

                        orderLaundryModel.setTotal(tlf.getTotal());
                        orderLaundryModel.setCategories(tlf.getCategories());

                        nextViewPager(viewPagerPosition, currentState);
                    } else if (fragment instanceof PickLocationFragment) {
                        PickLocationFragment pcf = (PickLocationFragment) fragment;

                        if (pcf.isInputValid()) {
                            String address = pcf.getAddress();
                            String addressDetail = pcf.getAddressDetail();
                            double lat = pcf.getLatitude();
                            double lng = pcf.getLongitude();

                            orderLaundryModel.setAddressPick(address);
                            orderLaundryModel.setAddressDetailPick(addressDetail);
                            orderLaundryModel.setLatPick(lat);
                            orderLaundryModel.setLngPick(lng);

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
                        cof.setTotal(orderLaundryModel.getTotal());

                        String addressPick = orderLaundryModel.getAddressDetailPick() + " | " + orderLaundryModel.getAddressPick();
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

                    orderLaundryModel.setStatus(0);

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
}
