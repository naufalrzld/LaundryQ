package com.motion.laundryq;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motion.laundryq.adapter.CategoryAdapter;
import com.motion.laundryq.adapter.ViewPagerAdapter;
import com.motion.laundryq.fragment.TypeLaundryFragment;
import com.motion.laundryq.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    btnNext.setText("Selesai");
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
                        Toast.makeText(OrderActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    step.setAllStatesCompleted(true);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Intent dataIntent = getIntent();
        Bundle bundle = new Bundle();

        List<CategoryModel> categories = dataIntent.getParcelableArrayListExtra(KEY_DATA_INTENT_CATEGORIES);
        String laundryName = dataIntent.getStringExtra(KEY_DATA_INTENT_LAUNDRY_NAME);

        bundle.putParcelableArrayList(KEY_DATA_INTENT_CATEGORIES, (ArrayList<? extends Parcelable>) categories);
        bundle.putString(KEY_DATA_INTENT_LAUNDRY_NAME, laundryName);

        TypeLaundryFragment typeLaundryFragment = new TypeLaundryFragment();
        typeLaundryFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(typeLaundryFragment, "Jenis Cucian");

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
