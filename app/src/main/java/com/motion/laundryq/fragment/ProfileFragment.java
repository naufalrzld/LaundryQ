package com.motion.laundryq.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.motion.laundryq.LoginActivity;
import com.motion.laundryq.MapActivity;
import com.motion.laundryq.R;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.USER_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView imgProfile;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
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
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    private SharedPreference sharedPreference;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreference = new SharedPreference(getContext());

        if (sharedPreference.checkIfDataExists(USER_PROFILE)) {
            UserModel userModel = sharedPreference.getObjectData(USER_PROFILE, UserModel.class);
            AddressModel addressModel = userModel.getAddress();
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
                startActivity(new Intent(getContext(), MapActivity.class));
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
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
        UserModel userModel = sharedPreference.getObjectData(USER_PROFILE, UserModel.class);
        AddressModel addressModel = userModel.getAddress();

        String address = "Alamat belum diatur";

        if (addressModel != null) {
            if (!TextUtils.isEmpty(addressModel.getAlamatDetail())) {
                address = addressModel.getAlamatDetail() + " | " + addressModel.getAlamat();
            } else {
                address = addressModel.getAlamat();
            }
        }

        tvAlamat.setText(address);
    }
}
