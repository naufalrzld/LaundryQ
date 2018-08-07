package com.motion.laundryq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.motion.laundryq.R;
import com.motion.laundryq.adapter.MyLaundryAdapter;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.OrderLaundryModel;
import com.motion.laundryq.utils.CurrencyConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ORDER_MODEL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_STATUS;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_id)
    TextView tvOrderID;
    @BindView(R.id.tv_date_order)
    TextView tvDateOrder;
    @BindView(R.id.tv_address_pickup)
    TextView tvAddressPickup;
    @BindView(R.id.tv_address_delivery)
    TextView tvAddressDelivery;
    @BindView(R.id.tv_time_pickup)
    TextView tvTimePickup;
    @BindView(R.id.tv_time_delivery)
    TextView tvTimeDelivery;
    @BindView(R.id.rv_laundry)
    RecyclerView rvLaundry;
    @BindView(R.id.tv_laundry_cost)
    TextView tvLaundryCost;
    @BindView(R.id.tv_admin_cost)
    TextView tvAdminCost;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private MyLaundryAdapter adapter;
    private OrderLaundryModel orderLaundryModel;
    private AddressModel addressPickModel, addressDeliveryModel;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_detail_order_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent dataIntent = getIntent();
        orderLaundryModel = dataIntent.getParcelableExtra(KEY_DATA_INTENT_ORDER_MODEL);
        addressPickModel = orderLaundryModel.getAddressPick();
        addressDeliveryModel = orderLaundryModel.getAddressDelivery();

        status = dataIntent.getIntExtra(KEY_DATA_INTENT_STATUS, 0);

        initView();
        setAdapter(status);
    }

    private void initView() {
        final String orderID = orderLaundryModel.getOrderID();
        String dateOrder = orderLaundryModel.getDateOrder();
        String pickupAddress = addressPickModel.getAlamatDetail() + " | " + addressPickModel.getAlamat();
        String deliveryAddress = addressDeliveryModel.getAlamatDetail() + " | " + addressDeliveryModel.getAlamat();
        String datePick = orderLaundryModel.getDatePickup();
        String dateDeliv = orderLaundryModel.getDateDelivery();
        String timePick = orderLaundryModel.getTimePickup();
        String timeDeliv = orderLaundryModel.getTimeDelivery();
        String dateTimePick = datePick + ", " + timePick;
        String dateTimeDeliv = dateDeliv + ", " + timeDeliv;
        String laundryCost = CurrencyConverter.toIDR(orderLaundryModel.getLaundryCost());
        String adminCost = CurrencyConverter.toIDR(orderLaundryModel.getAdminCost());
        String total = CurrencyConverter.toIDR(orderLaundryModel.getTotal());

        tvOrderID.setText(orderID);
        tvDateOrder.setText(dateOrder);
        tvAddressPickup.setText(pickupAddress);
        tvAddressDelivery.setText(deliveryAddress);
        tvTimePickup.setText(dateTimePick);
        tvTimeDelivery.setText(dateTimeDeliv);
        tvLaundryCost.setText(laundryCost);
        tvAdminCost.setText(adminCost);
        tvTotal.setText(total);
    }

    private void setAdapter(int status) {
        adapter = new MyLaundryAdapter(this, status);
        rvLaundry.setHasFixedSize(true);
        rvLaundry.setLayoutManager(new LinearLayoutManager(this));
        rvLaundry.setAdapter(adapter);

        adapter.setCategories(orderLaundryModel.getCategories());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
