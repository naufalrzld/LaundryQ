package com.motion.laundryq;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motion.laundryq.adapter.TimeOperationalAdapter;
import com.motion.laundryq.model.LaundryModel;
import com.motion.laundryq.model.TimeOperationalModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_MODEL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;

public class DetailLaundryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_laundry)
    ImageView imgLaundry;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_no_tlp)
    TextView tvNoTlp;
    @BindView(R.id.tv_id_line)
    TextView tvIdLine;
    @BindView(R.id.list_time_operational)
    ListView listTimeOperational;
    @BindView(R.id.lyt_book)
    LinearLayout lytBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laundry);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent dataIntent = getIntent();
        final LaundryModel laundryModel = dataIntent.getParcelableExtra(KEY_DATA_INTENT_LAUNDRY_MODEL);

        String laundryName = laundryModel.getLaundryName();
        String urlPhoto = laundryModel.getUrlPhoto();
        String address = laundryModel.getLocation().getAddress();
        String addressDetail = laundryModel.getLocation().getAddressDetail();
        String addressComplete;

        if (!TextUtils.isEmpty(addressDetail)) {
            addressComplete = addressDetail + " | " + address;
        } else {
            addressComplete = address;
        }

        String phoneNumber = laundryModel.getPhoneNumber();
        String idLine = laundryModel.getIdLine();

        if (TextUtils.isEmpty(idLine)) {
            idLine = "-";
        }

        List<TimeOperationalModel> listTime = laundryModel.getTimeOperational();

        getSupportActionBar().setTitle(laundryName);
        Glide.with(this).load(urlPhoto).into(imgLaundry);
        tvAddress.setText(addressComplete);
        tvNoTlp.setText(phoneNumber);
        tvIdLine.setText(idLine);

        TimeOperationalAdapter adapter = new TimeOperationalAdapter(this, listTime);
        listTimeOperational.setAdapter(adapter);

        justifyListViewHeightBasedOnChildren(listTimeOperational);

        lytBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailLaundryActivity.this, OrderActivity.class);
                intent.putExtra(KEY_DATA_INTENT_LAUNDRY_NAME, laundryModel.getLaundryName());
                intent.putParcelableArrayListExtra(KEY_DATA_INTENT_CATEGORIES, (ArrayList<? extends Parcelable>) laundryModel.getCategories());
                startActivity(intent);
            }
        });
    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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
