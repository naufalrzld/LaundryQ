package com.motion.laundryq;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.motion.laundryq.fragment.HomeFragment;
import com.motion.laundryq.fragment.OrderFragment;
import com.motion.laundryq.fragment.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottomNavView)
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        bottomNavView.setOnNavigationItemSelectedListener(mOnNavItemSelectedListener);

        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    if (!(currentFragment instanceof HomeFragment)) {
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
                case R.id.nav_order:
                    if (!(currentFragment instanceof OrderFragment)) {
                        fragment = new OrderFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
                case R.id.nav_profile:
                    if (!(currentFragment instanceof ProfileFragment)) {
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
