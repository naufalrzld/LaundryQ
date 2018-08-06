package com.motion.laundryq.fragment.order;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.motion.laundryq.R;
import com.motion.laundryq.model.AddressModel;
import com.motion.laundryq.model.UserModel;
import com.motion.laundryq.utils.SharedPreference;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryLocationFragment extends Fragment implements OnMapReadyCallback {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.btn_set_location)
    Button btnSetLocation;
    @BindView(R.id.til_address)
    TextInputLayout tilAddress;
    @BindView(R.id.et_address)
    TextInputEditText etAddress;
    @BindView(R.id.til_address_detail)
    TextInputLayout tilAddressDetail;
    @BindView(R.id.et_address_detail)
    TextInputEditText etAddressDetail;

    private static final String TAG = PickLocationFragment.class.getSimpleName();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean locationPermissionGranted = false;

    private GoogleMap mGoogleMap;

    private double latitude, longitude;

    public DeliveryLocationFragment() {
        // Required empty public constructor
    }

    public String getAddress() {
        return etAddress.getText().toString();
    }

    public String getAddressDetail() {
        return etAddressDetail.getText().toString();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_delivery_location, container, false);
        ButterKnife.bind(this, v);

        getLocationPermission();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = getCompleteAddressString(latitude, longitude);
                String enter[] = address.split("\n");
                etAddress.setText(enter[0]);
                etAddress.setSelection(enter[0].length());
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (locationPermissionGranted) {
            getLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
                    LatLng latLng = cameraPosition.target;
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
            });
        }
    }

    public boolean isInputValid() {
        tilAddress.setErrorEnabled(false);

        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            tilAddress.setErrorEnabled(true);
            tilAddress.setError("Alamat harus diisi");
            return false;
        }

        return true;
    }

    public void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat " + latLng.latitude + ", lng: " + latLng.longitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        latitude = latLng.latitude;
        longitude = latLng.longitude;

    }

    private void getLocation() {
        SharedPreference sharedPreference = new SharedPreference(getContext());
        UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
        AddressModel addressModel = userModel.getAddress();
        if (addressModel != null) {
            if (locationPermissionGranted) {
                etAddress.setText(addressModel.getAlamat());
                etAddressDetail.setText(addressModel.getAlamatDetail());

                moveCamera(new LatLng(addressModel.getLatitude(), addressModel.getLongitude()), DEFAULT_ZOOM);
            }
        }
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d(TAG, strReturnedAddress.toString());
            } else {
                Log.d(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }

        return strAdd;
    }
}
