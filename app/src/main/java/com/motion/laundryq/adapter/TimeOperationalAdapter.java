package com.motion.laundryq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.motion.laundryq.R;
import com.motion.laundryq.model.TimeOperationalModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeOperationalAdapter extends ArrayAdapter<TimeOperationalModel> {
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_time_operational)
    TextView tvTimeOperational;

    public TimeOperationalAdapter(Context context, List<TimeOperationalModel> timeList) {
        super(context, 0, timeList);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TimeOperationalModel timeOperationalModel = getItem(position);

        if(view==null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.time_operational_item, parent, false);
            ButterKnife.bind(this, view);
        }

        assert timeOperationalModel != null;
        String day = timeOperationalModel.getDay();
        String timeOpen = timeOperationalModel.getTimeOpen();
        String timeClose = timeOperationalModel.getTimeClose();
        String timeOperational = timeOpen + " - " + timeClose;

        tvDay.setText(day);
        tvTimeOperational.setText(timeOperational);

        return view;
    }
}
