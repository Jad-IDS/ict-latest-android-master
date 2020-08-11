package com.ids.ict.classes;

public class report {
	
	private int MobileNum;
	private String email;
	private String issue;
	private String comments;
	private String serviceprovider;
	private String location;
	
	public report(){
		
	}
	public report( int MobileNum, String email, String issue, String comments, String serviceprovider, String location){
		
		this.MobileNum=MobileNum;
		this.email=email;
		this.issue=issue;
		this.comments=comments;
		this.serviceprovider=serviceprovider;
		this.location=location;
		

	}
	public int getMobileNum() {
		return MobileNum;
	}
	public void setMobileNum(int MobileNum) {
		this.MobileNum = MobileNum;
	}

	public String getMail() {
		return email;
	}
	public void setMail(String email) {
		this.email = email;
	}

	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getServiceProvider() {
		return serviceprovider;
	}
	public void setServiceProvider(String serviceprovider) {
		this.serviceprovider = serviceprovider;
	}
	
	public String getLocat() {
		return location;
	}
	public void setLocat(String location) {
		this.location = location;
	}
	
}
