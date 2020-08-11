package com.ids.ict.classes;

import com.ids.ict.Actions;

public class MapLabel {
	private String imageName;
	private String label;
	private String number;
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = Actions.imageNameClean(imageName);
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setNumber(String n)
	{
		this.number = n;
		
		
	}
	public String getNumber()
	{
		return number;
	}
	

}
