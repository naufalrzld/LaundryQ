package com.motion.laundryq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreference sharedPreference = new SharedPreference(SplashscreenActivity.this);
                boolean isLogin = sharedPreference.isLoggedIn();
                Intent intent;

                if (isLogin) {
                    UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);

                    AuthCredential credential = EmailAuthProvider.getCredential(userModel.getEmail(), userModel.getPassword());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.reauthenticate(credential);

                    intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                }

                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
