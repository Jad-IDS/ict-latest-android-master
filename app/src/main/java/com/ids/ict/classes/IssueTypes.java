package com.ids.ict.classes;

public class IssueTypes {
    private String id;
    private String name;
    private boolean showOnMap;
    private boolean LocationMandatory;
    private boolean ServiceProviderTransfer;
    private boolean roaming;
    private String Logo;
    private boolean mainIssueId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowOnMap() {
        return showOnMap;
    }

    public void setShowOnMap(boolean showOnMap) {
        this.showOnMap = showOnMap;
    }

    public boolean isLocationMandatory() {
        return LocationMandatory;
    }

    public void setLocationMandatory(boolean locationMandatory) {
        LocationMandatory = locationMandatory;
    }

    public boolean isServiceProviderTransfer() {
        return ServiceProviderTransfer;
    }

    public void setServiceProviderTransfer(boolean serviceProviderTransfer) {
        ServiceProviderTransfer = serviceProviderTransfer;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public boolean isMainIssueId() {
        return mainIssueId;
    }

    public void setMainIssueId(boolean mainIssueId) {
        this.mainIssueId = mainIssueId;
    }

    public String toString() {
        return name;
    }

    public IssueTypes(String n) {
        this.name = n;
    }

    public IssueTypes() {
        this.id = "0";
        this.showOnMap = true;
    }

}
