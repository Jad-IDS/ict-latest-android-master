package com.ids.ict.classes;

public class Mail_OFF {

    private int id_rep;
    private String Id;
    private String number;
    private String email;
    private String qatarID;
    private String lang;
    private String loc;
    private String comm;
    private String date;
    private String date_aff;
    private String issuename;
    private String password;
    private String status;
    private String issueid;
    private String spid;
    private String locx;
    private String locy;
    private String countryid;
    private String affqatariid;
    private String spname;
    private String token;
    private String zone;
    private String building;
    private String street;
    private String areaId;
    private String subAreaId;
    private String dateOfRequest;
    private String specialneednumber;
    private String isSpecialNeed;
    private String channelUsedId;
    private String isCoorporate;
    private String isIndividual;
    private String calldate;
    private String callNum;
    private String cmplNum,cmplDate;
    private String longWait;
    private String affectedNumber;
    private String contactNumber;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAffectedNumber() {
        return affectedNumber;
    }

    public void setAffectedNumber(String affectedNumber) {
        this.affectedNumber = affectedNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLongWait() {
        return longWait;
    }

    public void setLongWait(String longWait) {
        this.longWait = longWait;
    }

    public String getCmplDate() {
        return cmplDate;
    }

    public void setCmplDate(String cmplDate) {
        this.cmplDate = cmplDate;
    }

    public String getCmplNum() {
        return cmplNum;
    }

    public void setCmplNum(String cmplNum) {
        this.cmplNum = cmplNum;
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

    public String getIsIndividual() {
        return isIndividual;
    }

    public void setIsIndividual(String isIndividual) {
        this.isIndividual = isIndividual;
    }

    public String getIsCoorporate() {
        return isCoorporate;
    }

    public void setIsCoorporate(String isCoorporate) {
        this.isCoorporate = isCoorporate;
    }

    public String getChannelUsedId() {
        return channelUsedId;
    }

    public void setChannelUsedId(String channelUsedId) {
        this.channelUsedId = channelUsedId;
    }

    public String getIsSpecialNeed() {
        return isSpecialNeed;
    }

    public void setIsSpecialNeed(String isSpecialNeed) {
        this.isSpecialNeed = isSpecialNeed;
    }

    public String getSpecialneednumber() {
        return specialneednumber;
    }

    public void setSpecialneednumber(String specialneednumber) {
        this.specialneednumber = specialneednumber;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(String subAreaId) {
        this.subAreaId = subAreaId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Mail_OFF() {

    }

    public Mail_OFF(int id_rep, String Id, String number, String email, String qatarID, String lang, String loc,
                    String comm, String date, String date_aff, String issuename, String status, String issueid,
                    String spid, String locx, String locy, String countid, String affqatarid, String spname) {

        this.id_rep = id_rep;
        this.Id = Id;
        this.number = number;
        this.email = email;
        this.qatarID = qatarID;
        this.lang = lang;
        this.loc = loc;
        this.comm = comm;
        this.date = date;
        this.date_aff = date_aff;
        this.issuename = issuename;
        this.status = status;
        this.issueid = issueid;
        this.spid = spid;
        this.locx = locx;
        this.locy = locy;
        this.countryid = countid;
        this.affqatariid = affqatarid;
        this.spname = spname;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setId_rep(int Id) {
        this.id_rep = Id;
    }

    public void setnum(String Title) {
        this.number = Title;
    }

    public void setissueid(String Title) {
        this.issueid = Title;
    }

    public void setspid(String Title) {
        this.spid = Title;
    }

    public void setlocx(String Title) {
        this.locx = Title;
    }

    public void setlocy(String Title) {
        this.locy = Title;
    }

    public void setcountid(String Title) {
        this.countryid = Title;
    }

    public void setaffcqatar(String Title) {
        this.affqatariid = Title;
    }

    public void setemail(String Date) {
        this.email = Date;
    }

    public void setstatus(String Date) {
        this.status = Date;
    }

    public void setspname(String Date) {
        this.spname = Date;
    }

    public void setqatarID(String Title) {
        this.qatarID = Title;
    }

    public void setdate_Aff(String Date_aff) {
        this.date_aff = Date_aff;
    }

    public void setissuename(String issuename) {
        this.issuename = issuename;
    }

    public void setlang(String Date) {
        this.lang = Date;
    }

    public void setloc(String loc) {
        this.loc = loc;
    }

    public void setcomm(String comm) {
        this.comm = comm;
    }


    public void setdate(String Date) {
        this.date = Date;
    }

    public String getId() {
        return this.Id;
    }

    public int getid_rep() {
        return this.id_rep;
    }

    public String getissueid() {
        return this.issueid;
    }

    public String getspname() {
        return this.spname;
    }

    public String getspid() {
        return this.spid;
    }

    public String getlocx() {
        return this.locx;
    }

    public String getlocy() {
        return this.locy;
    }

    public String getcountid() {
        return this.countryid;
    }

    public String getaffecqatarid() {
        return this.affqatariid;
    }

    public String getnum() {
        return this.number;
    }

    public String getemail() {
        return this.email;
    }

    public String getstatus() {
        return this.status;
    }

    public String getqatarID() {
        return this.qatarID;
    }

    public String getdate_aff() {
        return this.date_aff;
    }

    public String getissuename() {
        return this.issuename;
    }


    public String getlang() {
        return this.lang;
    }

    public String getloc() {
        return this.loc;
    }

    public String getdate() {
        return this.date;
    }

    public String getcomm() {
        return this.comm;
    }

    public String toString() {
        return "\nid= " + this.Id + "\nTitle= " + this.number + "\nbody= " + this.email + "\nDate= " + this.qatarID + "\nthumbnailUrl= " + this.lang;
    }
}
