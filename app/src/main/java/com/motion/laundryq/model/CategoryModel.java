package com.motion.laundryq.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {
    private String categoryID;
    private String categoryName;
    private String categoryUnit;
    private String icon;
    private Integer categoryPrice;
    private int quantity;
    private Integer status;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        dest.writeString(this.icon);
        dest.writeValue(this.categoryPrice);
        dest.writeInt(this.quantity);
        dest.writeValue(this.status);
    }

    protected CategoryModel(Parcel in) {
        this.categoryID = in.readString();
        this.categoryName = in.readString();
        this.categoryUnit = in.readString();
        this.icon = in.readString();
        this.categoryPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantity = in.readInt();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
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
