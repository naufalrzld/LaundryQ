package com.motion.laundryq.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class LaundryModel implements Parcelable {
    private Boolean active;
    private String idLine;
    private String laundryID;
    private String laundryName;
    private Boolean open;
    private String owner;
    private String phoneNumber;
    private String urlPhoto;
    private Boolean deliveryOder;
    private LaundryLocationModel location;
    private List<CategoryModel> categories;
    private List<TimeOperationalModel> timeOperational;

    public LaundryModel() {
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getIdLine() {
        return idLine;
    }

    public void setIdLine(String idLine) {
        this.idLine = idLine;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getLaundryName() {
        return laundryName;
    }

    public void setLaundryName(String laundryName) {
        this.laundryName = laundryName;
    }

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Boolean getDeliveryOder() {
        return deliveryOder;
    }

    public void setDeliveryOder(Boolean deliveryOder) {
        this.deliveryOder = deliveryOder;
    }

    public LaundryLocationModel getLocation() {
        return location;
    }

    public void setLocation(LaundryLocationModel location) {
        this.location = location;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<TimeOperationalModel> getTimeOperational() {
        return timeOperational;
    }

    public void setTimeOperational(List<TimeOperationalModel> timeOperational) {
        this.timeOperational = timeOperational;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.active);
        dest.writeString(this.idLine);
        dest.writeString(this.laundryID);
        dest.writeString(this.laundryName);
        dest.writeValue(this.open);
        dest.writeString(this.owner);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.urlPhoto);
        dest.writeValue(this.deliveryOder);
        dest.writeParcelable(this.location, flags);
        dest.writeList(this.categories);
        dest.writeTypedList(this.timeOperational);
    }

    protected LaundryModel(Parcel in) {
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.idLine = in.readString();
        this.laundryID = in.readString();
        this.laundryName = in.readString();
        this.open = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.owner = in.readString();
        this.phoneNumber = in.readString();
        this.urlPhoto = in.readString();
        this.deliveryOder = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.location = in.readParcelable(LaundryLocationModel.class.getClassLoader());
        this.categories = new ArrayList<CategoryModel>();
        in.readList(this.categories, CategoryModel.class.getClassLoader());
        this.timeOperational = in.createTypedArrayList(TimeOperationalModel.CREATOR);
    }

    public static final Parcelable.Creator<LaundryModel> CREATOR = new Parcelable.Creator<LaundryModel>() {
        @Override
        public LaundryModel createFromParcel(Parcel source) {
            return new LaundryModel(source);
        }

        @Override
        public LaundryModel[] newArray(int size) {
            return new LaundryModel[size];
        }
    };
}
