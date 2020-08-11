package com.ids.ict.classes;


import android.graphics.Bitmap;

public class IssuesType {
		private int Id;
		private String Name;
		private String ShowOnMap;
		private String LocationMandatory;
		private String ServiceProviderTransfer;
		private String Roaming;
		private String MainIssueId;
		
		public IssuesType(){
			
		}
		
		public IssuesType(int Id,String Name,String ShowOnMap, String LocationMandatory, String ServiceProviderTransfer, String Roaming, String MainIssueId){
			this.Id = Id;
			this.Name = Name;
			this.ShowOnMap = ShowOnMap;
			this.LocationMandatory = LocationMandatory;
			this.ServiceProviderTransfer = ServiceProviderTransfer;
			this.Roaming = Roaming;
			this.MainIssueId = MainIssueId;
		}
		
		public void setId(int Id){
			this.Id=Id;
		}
		
		public void setName(String Name){
			this.Name = Name;
		}
		
		
		
		public void setServiceProviderTransfer(String ServiceProviderTransfer){
			this.ServiceProviderTransfer = ServiceProviderTransfer;
		}
		
		public void setShowOnMap(String ShowOnMap){
			this.ShowOnMap = ShowOnMap;
		}
		
		public void setRoaming(String Roaming){
			this.Roaming = Roaming;
		}
		
		public void setMainIssueId(String MainIssueId){
			this.MainIssueId = MainIssueId;
		}
		public void setLocationMandatory(String LocationMandatory){
			this.LocationMandatory = LocationMandatory;
		}
		
		public int getId(){
			return this.Id;
		}
		
		public String getName(){
			return this.Name ;
		}
		
		public String getServiceProviderTransfer(){
			return this.ServiceProviderTransfer ;
		}

		public String getShowOnMap(){
			return this.ShowOnMap ;
		}
		
		public String getRoaming(){
			return this.Roaming ;
		}
		public String getMainIssueId(){
			return this.MainIssueId ;
		}
		public String getLocationMandatory(){
			return this.LocationMandatory ;
		}
		
		
		/* public String toString(){
			return "\nid= " + this.Id + "\nTitle= "+this.Name + "\nbody= "+this.Body + "\nDate= "+ this.Date + "\nthumbnailUrl= "+this.ThumbnailUrl + "\nImageUrl= "+this.ImageUrl + "\nTypeId= "+this.TypeId;
		}*/
	}


