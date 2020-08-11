package com.ids.ict.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QosNotification implements Parcelable {

    private int id, scheduleId, freq;
    private String testTriggerType, testTriggerId, testType, dateCreated;

    public QosNotification(){

        id = -1;
        scheduleId = -1;
        freq = 500;
        testTriggerId = testTriggerType = dateCreated = "";
    }

    public QosNotification(int id, int scheduleId, int freq, String testTriggerType, String testTriggerId, String testType, String dateCreated) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.freq = freq;
        this.testTriggerType = testTriggerType;
        this.testTriggerId = testTriggerId;
        this.testType = testType;
        this.dateCreated = dateCreated;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public String getTestTriggerType() {
        return testTriggerType;
    }

    public void setTestTriggerType(String testTriggerType) {
        this.testTriggerType = testTriggerType;
    }

    public String getTestTriggerId() {
        return testTriggerId;
    }

    public void setTestTriggerId(String testTriggerId) {
        this.testTriggerId = testTriggerId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDate(){

        long milliseconds;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
            Date d = f.parse(dateCreated);
            milliseconds = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            milliseconds = 0;
        }

        return new Date(milliseconds);
    }

    public String getDateFormatted(){

        String formattedDate = "";
        SimpleDateFormat output = new SimpleDateFormat("yyyy-M-dd HH:mm:dd", Locale.ENGLISH);
        formattedDate = output.format(getDate());
        return  formattedDate;
    }

    @Override
    public String toString() {
        return "(" +
                "scheduleId=" + scheduleId +
                ", freq=" + freq +
                ", testTriggerType='" + testTriggerType + '\'' +
                ", testTriggerId='" + testTriggerId + '\'' +
                ", testType='" + testType + '\'' +
                ", dateCreated=" + dateCreated +
                ')';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(scheduleId);
        dest.writeInt(freq);
        dest.writeString(testTriggerType);
        dest.writeString(testTriggerId);
        dest.writeString(testType);
        dest.writeString(dateCreated);
    }

    // "De-parcel object
    public QosNotification(Parcel in) {

        id = in.readInt();
        scheduleId = in.readInt();
        freq = in.readInt();
        testTriggerType = in.readString();
        testTriggerId = in.readString();
        testType = in.readString();
        dateCreated = in.readString();
    }

    public static final Creator CREATOR
            = new Creator() {
        public QosNotification createFromParcel(Parcel in) {
            return new QosNotification(in);
        }

        public QosNotification[] newArray(int size) {
            return new QosNotification[size];
        }
    };


}
