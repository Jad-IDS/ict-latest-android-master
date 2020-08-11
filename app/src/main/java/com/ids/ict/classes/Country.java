package com.ids.ict.classes;


import android.graphics.Bitmap;

public class Country {
		private String Id;
		private String Name;
		private String lang;
		
		public Country(){
			
		}
		
		public Country(String Id,String Name,String lang){
			this.Id = Id;
			this.Name = Name;
			this.lang=lang;
		}
		
		public void setId(String Id){
			this.Id=Id;
		}
		
		public void setName(String Name){
			this.Name = Name;
		}
		
		
		public void setlang(String Id){
			this.lang=Id;
		}
		
		
		
		
		
		public String getId(){
			return this.Id;
		}
		
		public String getName(){
			return this.Name ;
		}
		
		public String getlang(){
			return this.lang ;
		}
	
		
		
		/* public String toString(){
			return "\nid= " + this.Id + "\nTitle= "+this.Name + "\nbody= "+this.Body + "\nDate= "+ this.Date + "\nthumbnailUrl= "+this.ThumbnailUrl + "\nImageUrl= "+this.ImageUrl + "\nTypeId= "+this.TypeId;
		}*/
	}


