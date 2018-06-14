package com.motion.laundryq;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_password_conf)
    TextInputLayout tilPasswordConf;
    @BindView(R.id.et_passwod)
    TextInputEditText etPassword;
    @BindView(R.id.et_passwod_conf)
    TextInputEditText etPasswordConf;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private FirebaseUser firebaseUser;

    private ProgressDialog changePasswordLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        changePasswordLoading = new ProgressDialog(this);
        changePasswordLoading.setMessage("Loading . . .");
        changePasswordLoading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_change_password_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                String passwordConf = etPasswordConf.getText().toString();

                if (isInputValid(password, passwordConf)) {
                    changePassword(password);
                }
            }
        });
    }

    private boolean isInputValid(String password, String passwordConf) {
        tilPassword.setErrorEnabled(false);
        tilPasswordConf.setErrorEnabled(false);

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConf)) {
            if (TextUtils.isEmpty(password)) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Masukkan kata sandi anda!");
            }

            if (TextUtils.isEmpty(passwordConf)) {
                tilPasswordConf.setErrorEnabled(true);
                tilPasswordConf.setError("Masukkan konfirmasi kata sandi!");
            }

            return false;
        } else {
            if (password.length() < 6) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Kata sandi minimal 6 karakter");

                return false;
            }

            if (!password.equals(passwordConf)) {
                tilPasswordConf.setErrorEnabled(true);
                tilPasswordConf.setError("Konfirmasi kata sandi tidak cocok!");

                return false;
            }

            return true;
        }
    }

    private void changePassword(String password) {
        changePasswordLoading.show();
        firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changePasswordLoading.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }
}
