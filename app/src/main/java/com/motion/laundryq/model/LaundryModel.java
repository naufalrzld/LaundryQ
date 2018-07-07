package com.motion.laundryq.model;

import java.util.List;

public class LaundryModel {
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
}
