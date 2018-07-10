package com.motion.laundryq.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LaundryLocationModel implements Parcelable {
    private String address;
    private String addressDetail;
    private double latitude;
    private double longitude;

    public LaundryLocationModel() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.addressDetail);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected LaundryLocationModel(Parcel in) {
        this.address = in.readString();
        this.addressDetail = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<LaundryLocationModel> CREATOR = new Parcelable.Creator<LaundryLocationModel>() {
        @Override
        public LaundryLocationModel createFromParcel(Parcel source) {
            return new LaundryLocationModel(source);
        }

        @Override
        public LaundryLocationModel[] newArray(int size) {
            return new LaundryLocationModel[size];
        }
    };
}
