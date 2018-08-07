package com.motion.laundryq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.motion.laundryq.R;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.FDB_KEY_URL_PHOTO;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER;
import static com.motion.laundryq.utils.AppConstant.FDB_KEY_USER_CUSTOMER;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_ADDRESS_DETAIL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_EMAIL;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_NO_TLP;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_EDIT;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_USER_ID;
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

    public static final int RESULT_LOAD_IMG = 1;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private SharedPreference sharedPreference;
    private UserModel userModel;
    private AddressModel addressModel;

    private String userID;

    private ProgressDialog updateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        updateLoading = new ProgressDialog(this);
        updateLoading.setMessage("Uploading photo . . .");
        updateLoading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_edit_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference(FDB_KEY_USER).child(FDB_KEY_USER_CUSTOMER);
        storageReference = FirebaseStorage.getInstance().getReference();

        sharedPreference = new SharedPreference(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMG);
            }
        });

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
                if (addressModel != null) {
                    intent.putExtra(KEY_DATA_INTENT_EDIT, true);
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
                    intent.putExtra(KEY_DATA_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_NO_TLP, userModel.getNoTlp());
                }

                startActivity(intent);
            }
        });

        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, EditEmailActivity.class);
                intent.putExtra(KEY_DATA_INTENT_EMAIL, userModel.getEmail());
                intent.putExtra(KEY_DATA_INTENT_USER_ID, userModel.getUserID());
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

    private void uploadPhoto(final String userID, Uri imageUri) {
        updateLoading.show();
        final StorageReference ref = storageReference.child("images/users/customer/" + userID);
        UploadTask uploadTask = ref.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("error", "ERROR");
                    return null;
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final String url = task.getResult().toString();

                    Map<String, Object> map = new HashMap<>();
                    map.put(FDB_KEY_URL_PHOTO, url);

                    userModel.setUrlPhoto(url);

                    databaseReference.child(userID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateLoading.dismiss();

                            if (task.isSuccessful()) {
                                sharedPreference.storeData(KEY_PROFILE, userModel);

                                Glide.with(getApplicationContext())
                                        .load(url)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(profileImage);

                                Toast.makeText(EditProfileActivity.this, "Foto berhasil diupdate", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Foto gagal diupdate", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Foto gagal diupload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            addressModel = userModel.getAddress();

            String addressComplete = "Alamat belum diatur";
            if (addressModel != null) {
                String address = addressModel.getAlamat();
                String addressDetail = addressModel.getAlamatDetail();
                addressComplete = addressDetail + " | " + address;
            }

            userID = userModel.getUserID();
            String name = userModel.getNama();
            String noTlp = userModel.getNoTlp();
            String email = userModel.getEmail();
            String urlPhoto = userModel.getUrlPhoto();

            tvNama.setText(name);
            tvAddress.setText(addressComplete);
            tvNoTlp.setText(noTlp);
            tvEmail.setText(email);

            if (!TextUtils.isEmpty(urlPhoto)) {
                Glide.with(this)
                        .load(urlPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImage);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            uploadPhoto(userID, selectedImage);
        }
    }
}
