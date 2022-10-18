package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String fullName;
    private String email;
    private String id;
    private String image;

    public User() {}

    public User(String fullName, String email, String id, String image) {
        this.fullName = fullName;
        this.email = email;
        this.id = id;
        this.image = image;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        id = in.readString();
        image = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(image);
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
