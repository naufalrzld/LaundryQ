package com.motion.laundryq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.lyt_display_name)
    LinearLayout lytDisplayName;
    @BindView(R.id.lyt_edit_name)
    LinearLayout lytEditName;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_change_name)
    TextView tvChangeName;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.tv_save)
    TextView tvSave;
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

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreference sharedPreference;
    private UserModel userModel;
    private String userID, name, address, addressDetail, noTlp, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_edit_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);

        sharedPreference = new SharedPreference(this);
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);

            userID = userModel.getUserID();
            name = userModel.getNama();
            address = userModel.getAddress().getAlamat();
            addressDetail = userModel.getAddress().getAlamatDetail();
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
