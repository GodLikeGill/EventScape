package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Orders implements Parcelable {
    private String id;
    private String eventId;
    private String eventImageThumb;
    private String eventTitle;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    private String userId;
    private String userEmail;
    private String numberOfTickets;
    private String ticketPrice;
    private String totalOrderPrice;
    private String orderDate;

    public Orders(){
    }

    public Orders(String id, String eventId, String eventImageThumb, String eventTitle, String eventLocation, String eventDate, String eventTime, String userId, String userEmail, String numberOfTickets, String ticketPrice, String totalOrderPrice, String orderDate) {
        this.id = id;
        this.eventId = eventId;

        this.eventImageThumb = eventImageThumb;
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;

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

        eventImageThumb = in.readString();
        eventTitle = in.readString();
        eventLocation = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();

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

    public String getEventImageThumb() {
        return eventImageThumb;
    }

    public void setEventImageThumb(String eventImageThumb) {
        this.eventImageThumb = eventImageThumb;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
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

        parcel.writeString(eventImageThumb);
        parcel.writeString(eventTitle);
        parcel.writeString(eventLocation);
        parcel.writeString(eventDate);
        parcel.writeString(eventTime);

        parcel.writeString(userId);
        parcel.writeString(userEmail);
        parcel.writeString(numberOfTickets);
        parcel.writeString(ticketPrice);
        parcel.writeString(totalOrderPrice);
        parcel.writeString(orderDate);
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventImageThumb='" + eventImageThumb + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", numberOfTickets='" + numberOfTickets + '\'' +
                ", ticketPrice='" + ticketPrice + '\'' +
                ", totalOrderPrice='" + totalOrderPrice + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
