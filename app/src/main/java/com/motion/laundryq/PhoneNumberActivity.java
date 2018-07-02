package com.motion.laundryq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_NO_TLP;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_NO_TLP;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_EDIT;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class PhoneNumberActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_no_tlp)
    EditText etNoTlp;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreference sharedPreference;

    private UserModel userModel;

    private ProgressDialog phoneNumberLaoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        ButterKnife.bind(this);

        phoneNumberLaoding = new ProgressDialog(this);
        phoneNumberLaoding.setMessage("Loading . . .");
        phoneNumberLaoding.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_phone_number_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);

        final String userID;

        Intent dataIntent = getIntent();
        if (dataIntent.getBooleanExtra(KEY_DATA_INTENT_EDIT, false)) {
            String phoneNumber = dataIntent.getStringExtra(KEY_DATA_INTENT_NO_TLP);
            etNoTlp.setText(phoneNumber);
            etNoTlp.setSelection(phoneNumber.length());
        }

        sharedPreference = new SharedPreference(this);
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            userID = userModel.getUserID();

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String noTlp = etNoTlp.getText().toString();

                    if (!TextUtils.isEmpty(noTlp)) {
                        savePhoneNumber(userID, noTlp);
                    } else {
                        etNoTlp.setError("Nomor telepon harus diisi!");
                    }
                }
            });
        }
    }

    private void savePhoneNumber(final String userID, String noTlp) {
        phoneNumberLaoding.show();
        databaseReference.child(userID).child(FDB_KEY_NO_TLP).setValue(noTlp);
        databaseReference.child(userID).child(FDB_KEY_NO_TLP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneNumberLaoding.dismiss();
                String noTlp = dataSnapshot.getValue(String.class);
                userModel.setNoTlp(noTlp);
                if (userModel != null) {
                    sharedPreference.storeData(KEY_PROFILE, userModel);

                    Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
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
