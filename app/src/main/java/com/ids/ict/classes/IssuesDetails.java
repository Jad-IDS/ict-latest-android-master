package com.ids.ict.classes;

public class IssuesDetails {
	private int Id;
	private String Name;
	private String issue_ID;
	private String modifyNum;
	private String waitDuration;
	private String waitUnit;
	private String waitUnitAr;
	private String lang;
	private String specialneedduration;
	private String specialneedUnit;
	private String specialneedUnitAr;
	private String checkappondate;

	public IssuesDetails() {

	}

	public String getCheckappondate() {
		return checkappondate;
	}

	public void setCheckappondate(String checkappondate) {
		this.checkappondate = checkappondate;
	}

	public String getSpecialNeedDuration() {
		return specialneedduration;
	}

	public void setSpecialNeedDuration(String need) {
		this.specialneedduration = need;
	}

	public IssuesDetails(int Id, String Name, String issue_id, String lang) {
		this.Id = Id;
		this.Name = Name;
		this.issue_ID = issue_id;
		this.lang = lang;

	}

	public String getSpecialneedUnitAr() {
		return specialneedUnitAr;
	}

	public void setSpecialneedUnitAr(String specialneedUnitAr) {
		this.specialneedUnitAr = specialneedUnitAr;
	}

	public String getSpecialneedUnit() {
		return specialneedUnit;
	}

	public void setSpecialneedUnit(String specialneedUnit) {
		this.specialneedUnit = specialneedUnit;
	}

	public void setId(int Id) {
		this.Id = Id;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public void setlang(String Name) {
		this.lang = Name;
	}

	public void setmodifyNum(String modifyNum) {
		this.modifyNum = modifyNum;
	}

	public void setShowOnMap(String issue_id) {
		this.issue_ID = issue_id;
	}

	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Name;
	}

	public String getmodifyNum() {
		return this.modifyNum;
	}

	public String getlang() {
		return this.lang;
	}

	public String getShowOnMap() {
		return this.issue_ID;
	}

	public void setWaitDuration(String waitDuration) {
		this.waitDuration = waitDuration;
	}

	public String getWaitDuration() {
		return this.waitDuration;
	}

	public void setWaitUnitAr(String waitUnit) {
		this.waitUnitAr = waitUnit;
	}

	public String getWaitUnitAr() {
		return this.waitUnitAr;
	}

	public void setWaitUnit(String waitUnit) {
		this.waitUnit = waitUnit;
	}

	public String getWaitUnit() {
		return this.waitUnit;
	}

}
