package com.motion.laundryq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_EMAIL;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_EMAIL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_USER_ID;

public class EditEmailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private ProgressDialog updateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        ButterKnife.bind(this);

        updateLoading = new ProgressDialog(this);
        updateLoading.setMessage("Updating email . . .");
        updateLoading.setCancelable(false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_change_email_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent dataIntent = getIntent();
        String emailIntent = dataIntent.getStringExtra(KEY_DATA_INTENT_EMAIL);
        final String userID = dataIntent.getStringExtra(KEY_DATA_INTENT_USER_ID);

        etEmail.setText(emailIntent);
        etEmail.setSelection(emailIntent.length());

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                if (isInputValid(email)) {
                    updateEmail(userID, email);
                }
            }
        });
    }

    private boolean isInputValid(String email) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Masukkan alamat email baru!");
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (!email.matches(emailPattern)) {
                etEmail.setError("Alamat email tidak valid!");
                return false;
            }

            return true;
        }

        return true;
    }

    private void updateEmail(final String userID, final String email) {
        updateLoading.show();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(FDB_KEY_EMAIL, email);

                    databaseReference.child(userID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateLoading.dismiss();
                            Toast.makeText(EditEmailActivity.this, "Email berhasil diubah", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    updateLoading.dismiss();
                    Toast.makeText(EditEmailActivity.this, "Email gagal diubah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
