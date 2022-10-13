package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    private String id;
    private String eventId;

    public Favorite(){
    }

    public Favorite(String id, String eventId) {
        this.id = id;
        this.eventId = eventId;
    }

    protected Favorite(Parcel in) {
        id = in.readString();
        eventId = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(eventId);
    }
}