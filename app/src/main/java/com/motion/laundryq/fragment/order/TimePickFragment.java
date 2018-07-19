package com.motion.laundryq.fragment.order;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motion.laundryq.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
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

    private Calendar[] mCalendar = new Calendar[2];
    private SimpleDateFormat sdfDate, sdfString;
    private Locale locale;

    private String[] mDate = new String[2];
    private String[] mTime = new String[2];
    private int status;

    public TimePickFragment() {
        // Required empty public constructor
    }

    public String getDatePickup() {
        return mDate[0];
    }

    public String getTimePickup() {
        return mTime[0];
    }

    public String getDateDelivery() {
        return mDate[1];
    }

    public String getTimeDelivery() {
        return mTime[1];
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_time_pick, container, false);
        ButterKnife.bind(this, v);

        mCalendar[0] = Calendar.getInstance();
        mCalendar[1] = Calendar.getInstance();

        locale = new Locale("in", "ID");

        sdfDate = new SimpleDateFormat("dd/MM/yyyy", locale);
        sdfString = new SimpleDateFormat("dd MMMM yyyy", locale);

        initView();

        return v;
    }

    private void initView() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", locale);

        Calendar calendar = Calendar.getInstance();
        mCalendar[1].set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        mCalendar[1].set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        mCalendar[1].set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        mCalendar[1].set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        mCalendar[1].set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        mDate[0] = mCalendar[0].get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar[0].get(Calendar.MONTH)
                + 1) + "/" + mCalendar[0].get(Calendar.YEAR);

        mDate[1] = mCalendar[1].get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar[1].get(Calendar.MONTH)
                + 1) + "/" + mCalendar[1].get(Calendar.YEAR);

        String datePickup = sdfString.format(mCalendar[0].getTime());
        String dateDelivery = sdfString.format(mCalendar[1].getTime());
        String time = sdfTime.format(calendar.getTime());

        mTime[0] = time;
        mTime[1] = time;

        etDatePick.setText(datePickup);
        etDateDelivery.setText(dateDelivery);
        etTimePick.setText(time);
        etTimeDelivery.setText(time);

        etDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TimePickFragment.this,
                        mCalendar[status].get(Calendar.YEAR),
                        mCalendar[status].get(Calendar.MONTH),
                        mCalendar[status].get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("Tanggal Pengambilan");
                dpd.setMinDate(Calendar.getInstance());
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        etTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
                Calendar cal = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        TimePickFragment.this,
                        mCalendar[status].get(Calendar.HOUR_OF_DAY),
                        mCalendar[status].get(Calendar.MINUTE),
                        false
                );
                tpd.setTitle("Jam Pengambilan");
                tpd.setMinTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
                tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });

        etDateDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, mCalendar[0].get(Calendar.YEAR));
                cal.set(Calendar.MONTH, mCalendar[0].get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, mCalendar[0].get(Calendar.DAY_OF_MONTH)+1);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TimePickFragment.this,
                        mCalendar[status].get(Calendar.YEAR),
                        mCalendar[status].get(Calendar.MONTH),
                        mCalendar[status].get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("Tanggal Pengiriman");
                dpd.setMinDate(cal);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        etTimeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, mCalendar[0].get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, mCalendar[0].get(Calendar.MINUTE));
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        TimePickFragment.this,
                        mCalendar[status].get(Calendar.HOUR_OF_DAY),
                        mCalendar[status].get(Calendar.MINUTE),
                        false
                );
                tpd.setTitle("Jam Pengiriman");
                tpd.setMinTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
                tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            if (status == 0) {
                mDate[status] = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;

                mCalendar[status].set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar[status].set(Calendar.MONTH, monthOfYear);
                mCalendar[status].set(Calendar.YEAR, year);

                String dateString = sdfString.format(sdfDate.parse(mDate[status]));
                etDatePick.setText(dateString);

                status++;

                mDate[status] = dayOfMonth+1 + "/" + (monthOfYear+1) + "/" + year;

                mCalendar[status].set(Calendar.DAY_OF_MONTH, dayOfMonth+1);
                mCalendar[status].set(Calendar.MONTH, monthOfYear);
                mCalendar[status].set(Calendar.YEAR, year);

                dateString = sdfString.format(sdfDate.parse(mDate[status]));
                etDateDelivery.setText(dateString);
            } else {
                mDate[status] = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;

                mCalendar[status].set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar[status].set(Calendar.MONTH, monthOfYear);
                mCalendar[status].set(Calendar.YEAR, year);

                String dateString = sdfString.format(sdfDate.parse(mDate[status]));
                etDateDelivery.setText(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (status == 0) {
            if (minute < 10) {
                mTime[status] = hourOfDay + ":" + "0" + minute;
                mTime[1] = hourOfDay + ":" + "0" + minute;
            } else {
                mTime[status] = hourOfDay + ":" + minute;
                mTime[1] = hourOfDay + ":" + minute;
            }

            mCalendar[status].set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar[status].set(Calendar.MINUTE, minute);

            mCalendar[1].set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar[1].set(Calendar.MINUTE, minute);

            etTimePick.setText(mTime[status]);
            etTimeDelivery.setText(mTime[1]);
        } else {
            if (minute < 10) {
                mTime[status] = hourOfDay + ":" + "0" + minute;
            } else {
                mTime[status] = hourOfDay + ":" + minute;
            }

            mCalendar[status].set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar[status].set(Calendar.MINUTE, minute);

            etTimeDelivery.setText(mTime[status]);
        }
    }
}
