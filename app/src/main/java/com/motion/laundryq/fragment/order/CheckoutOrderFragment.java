package com.motion.laundryq.fragment.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.motion.laundryq.R;
import com.motion.laundryq.adapter.MyLaundryAdapter;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.utils.CurrencyConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_INTENT_CHECKOUT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutOrderFragment extends Fragment {
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
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private MyLaundryAdapter adapter;

    private String dateOrder;

    public CheckoutOrderFragment() {
        // Required empty public constructor
    }

    public void setOrderID(String orderID) {
        tvOrderID.setText(orderID);
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setCategories(List<CategoryModel> categories) {
        adapter.setCategories(categories);
    }

    public void setAddressPickup(String addressPickup) {
        tvAddressPickup.setText(addressPickup);
    }

    public void setAddressDelivery(String addressDelivery) {
        tvAddressDelivery.setText(addressDelivery);
    }

    public void setTimePickup(String timePickup) {
        tvTimePickup.setText(timePickup);
    }

    public void setTimeDelivery(String timeDelivery) {
        tvTimeDelivery.setText(timeDelivery);
    }

    public void setTotal(int total) {
        String totalString = CurrencyConverter.toIDR(total);
        tvTotal.setText(totalString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_order, container, false);
        ButterKnife.bind(this, v);

        adapter = new MyLaundryAdapter(getContext(), KEY_INTENT_CHECKOUT);
        rvLaundry.setHasFixedSize(true);
        rvLaundry.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLaundry.setAdapter(adapter);

        Locale locale = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
        Calendar calendar = Calendar.getInstance();

        dateOrder = sdf.format(calendar.getTime());
        tvDateOrder.setText(dateOrder);

        return v;
    }

}
