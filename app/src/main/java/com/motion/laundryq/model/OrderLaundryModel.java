package com.motion.laundryq.model;

import java.util.ArrayList;
import java.util.List;

public class OrderLaundryModel {
    private String addressPick;
    private String addressDetailPick;
    private double latPick;
    private double lngPick;
    private int total;
    private List<CategoryModel> categories;

    public String getAddressPick() {
        return addressPick;
    }

    public void setAddressPick(String addressPick) {
        this.addressPick = addressPick;
    }

    public String getAddressDetailPick() {
        return addressDetailPick;
    }

    public void setAddressDetailPick(String addressDetailPick) {
        this.addressDetailPick = addressDetailPick;
    }

    public double getLatPick() {
        return latPick;
    }

    public void setLatPick(double latPick) {
        this.latPick = latPick;
    }

    public double getLngPick() {
        return lngPick;
    }

    public void setLngPick(double lngPick) {
        this.lngPick = lngPick;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
