package com.ids.ict.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Amal on 11/9/2017.
 */

public class QosTest implements Parcelable{

    private int id, testSource, notificationId;
    private String  userId, deviceId, serviceProvider, locationX, locationY, ip, locationIPAddress, timeStamp, testType, callDisconnectionReason;
    private String testDateTime, isIncident, signalStrength, connectionType, callDuration, testTriggerType, triggerStartDate, ValidationEndDate, resultId, statusId;
    private String deviceModel, deviceType, mobileNumber, adHocRequestId;
    private String locality, subLocality, route, province, spectrumSignalStrength, signalQuality;
    private boolean isSignalStrength;

    public QosTest(){

    }

    public String getSpectrumSignalStrength() {
        return spectrumSignalStrength;
    }

    public void setSpectrumSignalStrength(String spectrumSignalStrength) {
        this.spectrumSignalStrength = spectrumSignalStrength;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getTestSource() {
        return testSource;
    }

    public void setTestSource(int testSource) {
        this.testSource = testSource;
    }

    public String getSignalQuality() {
        return signalQuality;
    }

    public void setSignalQuality(String signalQuality) {
        this.signalQuality = signalQuality;
    }

    public String getAdHocRequestId() {
        return adHocRequestId;
    }

    public void setAdHocRequestId(String adHocRequestId) {
        this.adHocRequestId = adHocRequestId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getLocationIPAddress() {
        return locationIPAddress;
    }

    public void setLocationIPAddress(String locationIPAddress) {
        this.locationIPAddress = locationIPAddress;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    public Date getDate(){
        return new Date(Long.parseLong(timeStamp));
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getCallDisconnectionReason() {
        return callDisconnectionReason;
    }

    public void setCallDisconnectionReason(String callDisconnectionReason) {
        this.callDisconnectionReason = callDisconnectionReason;
    }

    public String getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(String testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getIsIncident() {
        return isIncident;
    }

    public void setIsIncident(String isIncident) {
        this.isIncident = isIncident;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getTestTriggerType() {
        return testTriggerType;
    }

    public void setTestTriggerType(String testTriggerType) {
        this.testTriggerType = testTriggerType;
    }

    public String getTriggerStartDate() {
        return triggerStartDate;
    }

    public void setTriggerStartDate(String triggerStartDate) {
        this.triggerStartDate = triggerStartDate;
    }

    public String getValidationEndDate() {
        return ValidationEndDate;
    }

    public void setValidationEndDate(String validationEndDate) {
        ValidationEndDate = validationEndDate;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isSignalStrength() {
        return isSignalStrength;
    }

    public void setSignalStrengthFlag(boolean signalStrength) {
        isSignalStrength = signalStrength;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "(" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", serviceProvider='" + serviceProvider + '\'' +
                ", locationX='" + locationX + '\'' +
                ", locationY='" + locationY + '\'' +
                ", ip='" + ip + '\'' +
                ", locationIPAddress='" + locationIPAddress + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", testType='" + testType + '\'' +
                ", callDisconnectionReason='" + callDisconnectionReason + '\'' +
                ", testDateTime='" + testDateTime + '\'' +
                ", isIncident='" + isIncident + '\'' +
                ", signalStrength='" + signalStrength + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ",  callDuration='" + callDuration + '\'' +
                ", testTriggerType='" + testTriggerType + '\'' +
                ", triggerStartDate='" + triggerStartDate + '\'' +
                ", ValidationEndDate='" + ValidationEndDate + '\'' +
                ", resultId='" + resultId + '\'' +
                ", statusId='" + statusId + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", adHocRequestId='" + adHocRequestId + '\'' +
                ", locality='" + locality + '\'' +
                ", subLocality='" + subLocality + '\'' +
                ", route='" + route + '\'' +
                ", province='" + province + '\'' +
                ", isSignalStrength=" + isSignalStrength +
                ')';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(testSource);
        dest.writeInt(notificationId);

        dest.writeString(userId);
        dest.writeString(deviceId);
        dest.writeString(serviceProvider);
        dest.writeString(locationX);
        dest.writeString(locationY);
        dest.writeString(ip);
        dest.writeString(locationIPAddress);
        dest.writeString(timeStamp);
        dest.writeString(testType);
        dest.writeString(callDisconnectionReason);
        dest.writeString(testDateTime);
        dest.writeString(isIncident);
        dest.writeString(signalStrength);
        dest.writeString(connectionType);
        dest.writeString(callDuration);
        dest.writeString(testTriggerType);
        dest.writeString(triggerStartDate);
        dest.writeString(ValidationEndDate);
        dest.writeString(resultId);
        dest.writeString(statusId);
        dest.writeString(deviceModel);
        dest.writeString(deviceType);
        dest.writeString(mobileNumber);
        dest.writeString(adHocRequestId);

        dest.writeString(locality);
        dest.writeString(subLocality);
        dest.writeString(route);
        dest.writeString(province);
        dest.writeString(spectrumSignalStrength);
        dest.writeString(signalQuality);

        dest.writeByte((byte) (isSignalStrength ? 1 : 0));
    }

    protected QosTest(Parcel in) {

        id = in.readInt();
        testSource = in.readInt();
        notificationId = in.readInt();

        userId = in.readString();
        deviceId = in.readString();
        serviceProvider = in.readString();
        locationX = in.readString();
        locationY = in.readString();
        ip = in.readString();
        locationIPAddress = in.readString();
        timeStamp = in.readString();
        testType = in.readString();
        callDisconnectionReason = in.readString();
        testDateTime = in.readString();
        isIncident = in.readString();
        signalStrength = in.readString();
        connectionType = in.readString();
        callDuration = in.readString();
        testTriggerType = in.readString();
        triggerStartDate = in.readString();
        ValidationEndDate = in.readString();
        resultId = in.readString();
        statusId = in.readString();
        deviceModel = in.readString();
        deviceType = in.readString();
        mobileNumber = in.readString();
        adHocRequestId = in.readString();
        locality = in.readString();
        subLocality = in.readString();
        route = in.readString();
        province = in.readString();
        spectrumSignalStrength = in.readString();
        signalQuality = in.readString();

        isSignalStrength = in.readByte() != 0;
    }

    public static final Creator<QosTest> CREATOR = new Creator<QosTest>() {
        @Override
        public QosTest createFromParcel(Parcel in) {
            return new QosTest(in);
        }

        @Override
        public QosTest[] newArray(int size) {
            return new QosTest[size];
        }
    };
}
