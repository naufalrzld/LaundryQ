package com.motion.laundryq.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.motion.laundryq.LoginActivity;
import com.motion.laundryq.R;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_alamat)
    TextView tvAlamat;
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
        if (sharedPreference.checkIfDataExists("profile")) {
            UserModel userModel = sharedPreference.getObjectData("profile", UserModel.class);

            tvNama.setText(userModel.getNama());
            tvAlamat.setText("Alamat belum diatur");
            tvNoTlp.setText(userModel.getNoTlp());
            tvEmail.setText(userModel.getEmail());
        }

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return view;
    }

    private void logout() {
        sharedPreference.clearAllData();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}
