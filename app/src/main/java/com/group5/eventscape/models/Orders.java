package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Orders implements Parcelable {
    private String id;
    private String eventId;
    private String userId;
    private String userEmail;
    private String numberOfTickets;
    private String ticketPrice;
    private String totalOrderPrice;
    private String orderDate;

    public Orders(){
    }

    public Orders(String id, String eventId, String userId, String userEmail, String numberOfTickets, String ticketPrice, String totalOrderPrice, String orderDate) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.numberOfTickets = numberOfTickets;
        this.ticketPrice = ticketPrice;
        this.totalOrderPrice = totalOrderPrice;
        this.orderDate = orderDate;
    }

    protected Orders(Parcel in) {
        id = in.readString();
        eventId = in.readString();
        userId = in.readString();
        userEmail = in.readString();
        numberOfTickets = in.readString();
        ticketPrice = in.readString();
        totalOrderPrice = in.readString();
        orderDate = in.readString();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(String numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
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
        parcel.writeString(userId);
        parcel.writeString(userEmail);
        parcel.writeString(numberOfTickets);
        parcel.writeString(ticketPrice);
        parcel.writeString(totalOrderPrice);
        parcel.writeString(orderDate);
    }
}
