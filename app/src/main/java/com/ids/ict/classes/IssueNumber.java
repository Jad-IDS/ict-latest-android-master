package com.ids.ict.classes;

public class IssueNumber {

		private String imageName;
		private String label;
		private String number;
		public String getImageName() {
			return imageName;
		}
		public IssueNumber()
		{
			number="0";
		}
		public void setImageName(String imageName) {
	this.imageName = imageName;
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