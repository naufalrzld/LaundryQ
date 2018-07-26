package com.motion.laundryq.model;

import java.util.List;

public class OrderLaundryModel {
    private String orderID;
    private String laundryID;
    private String userID;
    private String addressPick;
    private String addressDetailPick;
    private String addressDeliv;
    private String addressDetailDeliv;
    private double latPick;
    private double lngPick;
    private double latDeliv;
    private double lngDeliv;
    private int total;
    private String datePickup;
    private String timePickup;
    private String dateDelivery;
    private String timeDelivery;
    private String dateOrder;
    private int status;
    private String laundryID_status;
    private List<CategoryModel> categories;

    public OrderLaundryModel() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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

    public String getAddressDeliv() {
        return addressDeliv;
    }

    public void setAddressDeliv(String addressDeliv) {
        this.addressDeliv = addressDeliv;
    }

    public String getAddressDetailDeliv() {
        return addressDetailDeliv;
    }

    public void setAddressDetailDeliv(String addressDetailDeliv) {
        this.addressDetailDeliv = addressDetailDeliv;
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

    public double getLatDeliv() {
        return latDeliv;
    }

    public void setLatDeliv(double latDeliv) {
        this.latDeliv = latDeliv;
    }

    public double getLngDeliv() {
        return lngDeliv;
    }

    public void setLngDeliv(double lngDeliv) {
        this.lngDeliv = lngDeliv;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDatePickup() {
        return datePickup;
    }

    public void setDatePickup(String datePickup) {
        this.datePickup = datePickup;
    }

    public String getTimePickup() {
        return timePickup;
    }

    public void setTimePickup(String timePickup) {
        this.timePickup = timePickup;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLaundryID_status() {
        return laundryID_status;
    }

    public void setLaundryID_status(String laundryID_status) {
        this.laundryID_status = laundryID_status;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
