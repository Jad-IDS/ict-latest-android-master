package com.ids.ict.activities;

import android.graphics.Bitmap;

public class Event {

    private int id;
    private String description;
    private String name;
    private String date;
    private String imageUrlDay;
    private String imageUrlNight;
    private String showMap;
    private String location;
    private Bitmap imageDay;
    private Bitmap imageNight;
    private String ThumbnailUrlDay;
    private String ThumbnailUrlNight;
    private byte[] imgDay;
    private byte[] imgNight;
    private String ServiceProviderTransfer;
    private boolean ShowOnMap;

    // used to read from database
    public Event() {

    }

    public boolean isShowOnMap() {
        return ShowOnMap;
    }

    public void setShowOnMap(boolean showOnMap) {
        ShowOnMap = showOnMap;
    }

    public String getServiceProviderTransfer() {
        return ServiceProviderTransfer;
    }

    public void setServiceProviderTransfer(String serviceProviderTransfer) {
        ServiceProviderTransfer = serviceProviderTransfer;
    }

    public Event(int id, String name, String location, String description, String showMap, String imageUrlDay, String imageUrlNight, String date, String ThumbnailUrlDay, String ThumbnailUrlNight, byte[] imgDay, byte[] imgNight) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.imageUrlDay = imageUrlDay;
        this.imageUrlNight = imageUrlNight;
        this.showMap = showMap;
        this.location = location;
        this.ThumbnailUrlDay = ThumbnailUrlDay;
        this.ThumbnailUrlNight = ThumbnailUrlNight;
        this.imgDay = imgDay;
        this.imgNight = imgNight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getimgDay() {
        return this.imgDay;
    }

    public void setimgDay(byte[] name) {
        this.imgDay = name;
    }

    public byte[] getimgNight() {
        return this.imgNight;
    }

    public void setimgNight(byte[] name) {
        this.imgNight = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setShowMap(String id) {
        this.showMap = id;
    }

    public String getShowMap() {
        return this.showMap;
    }

    public void setImageUrlDay(String imageUrl) {
        this.imageUrlDay = imageUrl;
    }

    public String getImageUrlDay() {
        return this.imageUrlDay;
    }

    public void setImageUrlNight(String imageUrl) {
        this.imageUrlNight = imageUrl;
    }

    public String getImageUrlNight() {
        return this.imageUrlNight;
    }

    public void setThumbnailUrlDay(String ThumbnailUrl) {
        this.ThumbnailUrlDay = ThumbnailUrl;
    }

    public String getThumbnailUrlDayl() {
        return this.ThumbnailUrlDay;
    }

    public void setThumbnailUrlNight(String ThumbnailUrl) {
        this.ThumbnailUrlNight = ThumbnailUrl;
    }

    public String getThumbnailUrlNight() {
        return this.ThumbnailUrlNight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public Bitmap getImageDay() {
        return this.imageDay;
    }

    public void setBitmapDay(Bitmap image) {
        this.imageDay = image;
    }

    public Bitmap getImageNight() {
        return this.imageNight;
    }

    public void setBitmapNight(Bitmap image) {
        this.imageNight = image;
    }

    public String toString() {
        return "Name= " + this.name + "\nBody= " + this.description + "\nDate= " + this.date;
    }

}
