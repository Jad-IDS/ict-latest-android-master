package com.ids.ict.classes;

public class MapPinLocation {
    private String locationX;
    private String locationY;
    private String locality;
    private String IssueType;
    private String IssueTypeAr;
    private String number;
    private String pinIcon;
    private String subLocality;
    private String subLocalityAr;

    public String getSubLocalityAr() {
        return subLocalityAr;
    }

    public String getIssueTypeAr() {
        return IssueTypeAr;
    }

    public void setIssueTypeAr(String issueTypeAr) {
        IssueTypeAr = issueTypeAr;
    }

    public void setSubLocalityAr(String subLocalityAr) {
        this.subLocalityAr = subLocalityAr;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getIssueType() {
        return IssueType;
    }

    public void setIssueType(String issueType) {
        IssueType = issueType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinIcon() {
        return pinIcon;
    }

    public void setPinIcon(String pinIcon) {
        this.pinIcon = pinIcon;
    }


}
