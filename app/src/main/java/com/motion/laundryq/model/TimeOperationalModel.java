package com.motion.laundryq.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeOperationalModel implements Parcelable {
    private String day;
    private String timeOpen;
    private String timeClose;

    public TimeOperationalModel() {
    }

    public TimeOperationalModel(String day, String timeOpen, String timeClose) {
        this.day = day;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeString(this.timeOpen);
        dest.writeString(this.timeClose);
    }

    protected TimeOperationalModel(Parcel in) {
        this.day = in.readString();
        this.timeOpen = in.readString();
        this.timeClose = in.readString();
    }

    public static final Parcelable.Creator<TimeOperationalModel> CREATOR = new Parcelable.Creator<TimeOperationalModel>() {
        @Override
        public TimeOperationalModel createFromParcel(Parcel source) {
            return new TimeOperationalModel(source);
        }

        @Override
        public TimeOperationalModel[] newArray(int size) {
            return new TimeOperationalModel[size];
        }
    };
}
