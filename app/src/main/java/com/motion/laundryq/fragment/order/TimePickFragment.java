package com.motion.laundryq.fragment.order;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motion.laundryq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickFragment extends Fragment {
    @BindView(R.id.til_date_pick)
    TextInputLayout tilDatePick;
    @BindView(R.id.til_time_pick)
    TextInputLayout tilTimePick;
    @BindView(R.id.til_date_delivery)
    TextInputLayout tilDateDelivery;
    @BindView(R.id.til_time_delivery)
    TextInputLayout tilTimeDelivery;
    @BindView(R.id.et_date_pick)
    TextInputEditText etDatePick;
    @BindView(R.id.et_time_pick)
    TextInputEditText etTimePick;
    @BindView(R.id.et_date_delivery)
    TextInputEditText etDateDelivery;
    @BindView(R.id.et_time_delivery)
    TextInputEditText etTimeDelivery;

    public TimePickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_time_pick, container, false);
        ButterKnife.bind(this, v);



        return v;
    }

    private void initView() {
        etDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
