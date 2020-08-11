package com.ids.ict.classes;



public class Error {
	private boolean state;
	private String message;
	
	public Error(){
		this.state=false;
		this.message="";
	}
	public Error(String testing){
		this.state=true;
		this.message=testing;
	}
	public void setState(boolean state){
		this.state=state;
	}
	
	public boolean getState(){
		return this.state;
	}
	
	public void setMessage(String message){
		this.message=message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public void reset()
	{
		this.state=false;
		this.message="";
	}

}
