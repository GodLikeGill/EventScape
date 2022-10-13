package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    private String fullName;
    private String id;

    public Users(){
    }

    public Users(String fullName, String id) {
        this.fullName = fullName;
        this.id = id;
    }

    protected Users(Parcel in){
        fullName = in.readString();
        id = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(id);
    }
}
