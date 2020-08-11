package com.ids.ict.classes;

import android.graphics.Bitmap;

public class Profile {
	private String Id;
	private String number;
	private String email;
	private String qatarID;
	private String lang;
	
	
	public Profile(){
		
	}
	
	public Profile(String Id,String number,String email,String qatarID,String lang){
		this.Id = Id;
		this.number = number;
		this.email = email;
		this.qatarID = qatarID;
		this.lang = lang;
		
	}
	
	public void setId(String Id){
		this.Id=Id;
	}
	
	public void setnum(String Title){
		this.number = Title;
	}
	
	
	public void setemail(String Date){
		this.email = Date;
	}
	
	public void setqatarID(String Title){
		this.qatarID = Title;
	}
	
	
	public void setlang(String Date){
		this.lang = Date;
	}
	
	public String getId(){
		return this.Id;
	}
	
	public String getnum(){
		return this.number ;
	}
	
	public String getemail(){
		return this.email;
	}
	
	public String getqatarID(){
		return this.qatarID ;
	}
	public String getlang(){
		return this.lang ;
	}
	
	
	public String toString(){
		return "\nid= " + this.Id + "\nTitle= "+this.number + "\nbody= "+this.email + "\nDate= "+ this.qatarID + "\nthumbnailUrl= "+this.lang;
	}
}
