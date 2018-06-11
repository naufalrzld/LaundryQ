package com.motion.laundryq.model;

public class AddressModel {
    private String alamat;
    private String alamatDetail;
    private double latitude;
    private double longitude;

    public AddressModel() {
    }

    public AddressModel(String alamat, String alamatDetail, double latitude, double longitude) {
        this.alamat = alamat;
        this.alamatDetail = alamatDetail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamatDetail() {
        return alamatDetail;
    }

    public void setAlamatDetail(String alamatDetail) {
        this.alamatDetail = alamatDetail;
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
}
