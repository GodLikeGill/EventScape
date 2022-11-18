package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment implements Parcelable {

    private String number;
    private String expiry;
    private String name;
    private String type;
    private String cvv;

    public Payment() {
    }

    public Payment(String number, String expiry, String name, String type, String cvv) {
        this.number = number;
        this.expiry = expiry;
        this.name = name;
        this.type = type;
        this.cvv = cvv;
    }

    protected Payment(Parcel in) {
        number = in.readString();
        expiry = in.readString();
        name = in.readString();
        type = in.readString();
        cvv = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(expiry);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(cvv);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "number='" + number + '\'' +
                ", expiry='" + expiry + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
