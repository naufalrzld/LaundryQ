package com.motion.laundryq.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.motion.laundryq.EditProfileActivity;
import com.motion.laundryq.LoginActivity;
import com.motion.laundryq.MapActivity;
import com.motion.laundryq.PhoneNumberActivity;
import com.motion.laundryq.R;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS_DETAIL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_NO_TLP;
import static com.motion.laundryq.utils.AppConstant.KEY_INTENT_EDIT;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView imgProfile;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.lyt_alamat)
    LinearLayout lytAlamat;
    @BindView(R.id.tv_alamat)
    TextView tvAlamat;
    @BindView(R.id.lyt_no_tlp)
    LinearLayout lytNoTlp;
    @BindView(R.id.tv_noTlp)
    TextView tvNoTlp;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.lyt_logout)
    LinearLayout lytLogout;

    private SharedPreference sharedPreference;
    private AddressModel addressModel;
    private UserModel userModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreference = new SharedPreference(getContext());

        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            addressModel = userModel.getAddress();
            String address = "Alamat belum diatur";

            if (addressModel != null) {
                if (!TextUtils.isEmpty(addressModel.getAlamatDetail())) {
                    address = addressModel.getAlamatDetail() + " | " + addressModel.getAlamat();
                } else {
                    address = addressModel.getAlamat();
                }
            }

            tvNama.setText(userModel.getNama());
            tvAlamat.setText(address);
            tvNoTlp.setText(userModel.getNoTlp());
            tvEmail.setText(userModel.getEmail());
        }

        lytAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                if (!TextUtils.isEmpty(addressModel.getAlamat())) {
                    intent.putExtra(KEY_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS, addressModel.getAlamat());
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS_DETAIL, addressModel.getAlamatDetail());
                }
                startActivity(intent);
            }
        });

        lytNoTlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PhoneNumberActivity.class);
                if (!TextUtils.isEmpty(userModel.getNoTlp())) {
                    intent.putExtra(KEY_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_NO_TLP, userModel.getNoTlp());
                }

                startActivity(intent);
            }
        });

        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        sharedPreference.clearAllData();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
        addressModel = userModel.getAddress();

        String address = "Alamat belum diatur";

        if (addressModel != null) {
            if (!TextUtils.isEmpty(addressModel.getAlamatDetail())) {
                address = addressModel.getAlamatDetail() + " | " + addressModel.getAlamat();
            } else {
                address = addressModel.getAlamat();
            }
        }

        tvAlamat.setText(address);
        tvNoTlp.setText(userModel.getNoTlp());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                startActivity(new Intent(getContext(), EditProfileActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
