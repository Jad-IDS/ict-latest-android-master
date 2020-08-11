package com.ids.ict.classes;

public class MobileConfiguration {
	
	private String id;
	private String key;
	private String value;

	
	
	public MobileConfiguration()
	{
		this.id="";
		this.key="";
		this.value="";
	}
	
	public void setId(String id)
	{
		this.id=id;
	}
	public void setkey(String key)
	{
		this.key=key;
	}
	public void setValue(String v)
	{
		this.value=v;
	}
	public String getId()
	{
		return id;
	}
	public String getValue()
	{
		return value;
	}
	public String getKey()
	{
		return key;
	}
}
