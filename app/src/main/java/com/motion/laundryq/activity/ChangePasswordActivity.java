package com.motion.laundryq.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motion.laundryq.R;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class ChangePasswordActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.til_last_password)
    TextInputLayout tilLastPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_password_conf)
    TextInputLayout tilPasswordConf;
    @BindView(R.id.et_last_passwod)
    TextInputEditText etLastPassword;
    @BindView(R.id.et_passwod)
    TextInputEditText etPassword;
    @BindView(R.id.et_passwod_conf)
    TextInputEditText etPasswordConf;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private FirebaseUser firebaseUser;

    private SharedPreference sharedPreference;
    private UserModel userModel;

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

        sharedPreference = new SharedPreference(this);
        userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
        final String lastPassword = userModel.getPassword();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etLastPassword.getText().toString();
                String newPassword = etPassword.getText().toString();
                String passwordConf = etPasswordConf.getText().toString();

                if (isInputValid(password, newPassword, passwordConf)) {
                    if (isLastPasswordCorrect(lastPassword, password)) {
                        changePassword(newPassword);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Password lama salah!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isInputValid(String lastPassword, String newPassword, String passwordConf) {
        tilLastPassword.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilPasswordConf.setErrorEnabled(false);

        if (TextUtils.isEmpty(lastPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(passwordConf)) {
            if (TextUtils.isEmpty(lastPassword)) {
                tilLastPassword.setErrorEnabled(true);
                tilLastPassword.setError("Masukkan kata sandi lama anda!");
            }

            if (TextUtils.isEmpty(newPassword)) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Masukkan kata sandi baru anda!");
            }

            if (TextUtils.isEmpty(passwordConf)) {
                tilPasswordConf.setErrorEnabled(true);
                tilPasswordConf.setError("Masukkan konfirmasi kata sandi!");
            }

            return false;
        } else {
            if (lastPassword.length() < 6) {
                tilLastPassword.setErrorEnabled(true);
                tilLastPassword.setError("Kata sandi minimal 6 karakter");

                return false;
            }

            if (newPassword.length() < 6) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Kata sandi minimal 6 karakter");

                return false;
            }

            if (!newPassword.equals(passwordConf)) {
                tilPasswordConf.setErrorEnabled(true);
                tilPasswordConf.setError("Konfirmasi kata sandi tidak cocok!");

                return false;
            }

            return true;
        }
    }

    private boolean isLastPasswordCorrect(String lastPassword, String password) {
        if (!lastPassword.equals(password)) {
            return false;
        }

        return true;
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
