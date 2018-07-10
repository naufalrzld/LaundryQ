package com.motion.laundryq.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {
    private String categoryID;
    private String categoryName;
    private String categoryUnit;
    private Integer categoryPrice;

    public CategoryModel() {
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

    public Integer getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(Integer categoryPrice) {
        this.categoryPrice = categoryPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryUnit);
        dest.writeValue(this.categoryPrice);
    }

    protected CategoryModel(Parcel in) {
        this.categoryID = in.readString();
        this.categoryName = in.readString();
        this.categoryUnit = in.readString();
        this.categoryPrice = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel source) {
            return new CategoryModel(source);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
