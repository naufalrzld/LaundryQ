package com.motion.laundryq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS_DETAIL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_NAME;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_NO_TLP;
import static com.motion.laundryq.utils.AppConstant.KEY_INTENT_EDIT;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_change_name)
    TextView tvChangeName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btn_edit_address)
    Button btnEditAddress;
    @BindView(R.id.tv_no_tlp)
    TextView tvNoTlp;
    @BindView(R.id.btn_edit_no_tlp)
    Button btnEditNoTlp;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.btn_edit_email)
    Button btnEditEmail;
    @BindView(R.id.btn_edit_password)
    Button btnEditPassword;

    private SharedPreference sharedPreference;
    private UserModel userModel;
    private AddressModel addressModel;
    private String userID, name, address, addressDetail, noTlp, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_edit_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreference = new SharedPreference(this);

        tvChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, ChangeNameActivity.class));
            }
        });

        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MapActivity.class);
                if (!TextUtils.isEmpty(addressModel.getAlamat())) {
                    intent.putExtra(KEY_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS, addressModel.getAlamat());
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS_DETAIL, addressModel.getAlamatDetail());
                }
                startActivity(intent);
            }
        });

        btnEditNoTlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, PhoneNumberActivity.class);
                if (!TextUtils.isEmpty(userModel.getNoTlp())) {
                    intent.putExtra(KEY_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_NO_TLP, userModel.getNoTlp());
                }

                startActivity(intent);
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            addressModel = userModel.getAddress();

            userID = userModel.getUserID();
            name = userModel.getNama();
            address = addressModel.getAlamat();
            addressDetail = addressModel.getAlamatDetail();
            String addressComplete = addressDetail + " | " + address;
            noTlp = userModel.getNoTlp();
            email = userModel.getEmail();

            tvNama.setText(name);
            tvAddress.setText(addressComplete);
            tvNoTlp.setText(noTlp);
            tvEmail.setText(email);
        }
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
