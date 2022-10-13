package com.group5.eventscape.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable{

    private String id;
    private String user;
    private String userName;
    private String phone;
    private String title;
    private String category;
    private String desc;
    private String address;
    private String city;
    private String province;
    private String postCode;
    private String date;
    private String time;
    private String price;
    private String image;

    public Event() {
    }

    public Event(String id, String user, String userName, String phone, String title, String category, String desc, String address, String city, String province, String postCode, String date, String time, String price, String image) {
        this.id = id;
        this.user = user;
        this.userName = userName;
        this.phone = phone;
        this.title = title;
        this.category = category;
        this.desc = desc;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postCode = postCode;
        this.date = date;
        this.time = time;
        this.price = price;
        this.image = image;
    }

    protected Event(Parcel in) {
        id = in.readString();
        user = in.readString();
        userName = in.readString();
        phone = in.readString();
        title = in.readString();
        category = in.readString();
        desc = in.readString();
        address = in.readString();
        city = in.readString();
        province = in.readString();
        postCode = in.readString();
        date = in.readString();
        time = in.readString();
        price = in.readString();
        image = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(userName);
        dest.writeString(phone);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(desc);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(postCode);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(price);
        dest.writeString(image);
    }
}

