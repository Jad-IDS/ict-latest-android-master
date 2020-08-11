package com.ids.ict.classes;

public class register {
	
	private String Qatariid;
	private int MobileNum;
	private String email;
	
	public register(){
		
	}
	public register(String Qatariid, int MobileNum, String email){
		this.Qatariid=Qatariid;
		this.MobileNum=MobileNum;
		this.email=email;

	}
	public String getId() {
		return Qatariid;
	}
	public void setId(String Qatariid) {
		this.Qatariid = Qatariid;
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

	public String toString()
	{
		return this.Qatariid;
	}
	
}

	

	
	


