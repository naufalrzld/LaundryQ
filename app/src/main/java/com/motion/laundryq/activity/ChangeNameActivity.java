package com.motion.laundryq.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq.R;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_NAME;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class ChangeNameActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private DatabaseReference databaseReference;
    private SharedPreference sharedPreference;

    private UserModel userModel;

    private ProgressDialog changeNameLaoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_change_name_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeNameLaoding = new ProgressDialog(this);
        changeNameLaoding.setMessage("Loading . . .");
        changeNameLaoding.setCancelable(false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);

        sharedPreference = new SharedPreference(this);
        userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);

        final String userID = userModel.getUserID();
        String name = userModel.getNama();

        etName.setText(name);
        etName.setSelection(name.length());

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilName.setErrorEnabled(false);
                String name = etName.getText().toString();

                if (!TextUtils.isEmpty(name)) {
                    saveName(userID, name);
                } else {
                    tilName.setErrorEnabled(true);
                    tilName.setError("Masukkan nama lengkap anda!");
                }
            }
        });
    }

    private void saveName(String userID, String name) {
        changeNameLaoding.show();
        databaseReference.child(userID).child(FDB_KEY_NAME).setValue(name);
        databaseReference.child(userID).child(FDB_KEY_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                changeNameLaoding.dismiss();
                String name = dataSnapshot.getValue(String.class);
                userModel.setNama(name);

                sharedPreference.storeData(KEY_PROFILE, userModel);

                Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
