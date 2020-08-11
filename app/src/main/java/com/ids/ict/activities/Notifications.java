package com.ids.ict.activities;

import android.graphics.Bitmap;

public class Notifications {
	private int id;
	private String description;
	private String name;
	private String date;
	private String lang;

	// used to read from database
	public Notifications(){
		
	}
	public Notifications(int id, String name, String date, String description,String lang)
	{
		this.id= id;
		this.name = name;
		this.date = date;
		this.description = description;
		this.lang = lang;
		
	}

	public void setId(int id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setlang(String name){
		this.lang = name;
	}
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getlang(){
		return this.lang;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	public void setdescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description ;
	}

	
	
	public String getDate(){
		return this.date ;
	}
	
	
	public String toString(){
		return "Name= "+this.name+"\nBody= "+this.description+"\nDate= "+this.date;
	}
	
}
