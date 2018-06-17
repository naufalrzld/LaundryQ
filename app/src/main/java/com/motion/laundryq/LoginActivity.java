package com.motion.laundryq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_passwod)
    TextInputEditText etPassword;
    @BindView(R.id.tv_lupa_password)
    TextView tvLupasPassword;
    @BindView(R.id.tv_daftar)
    TextView tvDaftar;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreference sharedPreference;
    private ProgressDialog loginLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginLoading = new ProgressDialog(this);
        loginLoading.setMessage("Loading . . .");
        loginLoading.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);

        sharedPreference = new SharedPreference(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (isInputValid(email, password)) {
                    loginUser(email, password);
                }
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isInputValid(String email, String password) {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)) {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError("Masukkan alamat email / nomor telepon");
            }

            if (TextUtils.isEmpty(password)) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Masukkan kata sandi");
            }

            return false;
        } else {
            if (password.length() < 6) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Password minimal 6 karakter");

                return false;
            }
        }

        return true;
    }

    private void loginUser(final String email, final String password) {
        loginLoading.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String split[] = email.split("@");
                            String userID = split[0];
                            getDataUser(userID, password);
                        } else {
                            loginLoading.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getDataUser(final String userID, final String password) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loginLoading.dismiss();
                UserModel userModel = dataSnapshot.child(userID).getValue(UserModel.class);
                assert userModel != null;
                userModel.setUserID(userID);
                userModel.setPassword(password);

                sharedPreference.storeData(KEY_PROFILE, userModel);
                sharedPreference.setLogin(true);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }
}
