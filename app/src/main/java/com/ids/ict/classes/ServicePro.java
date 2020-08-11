package com.ids.ict.classes;


import android.graphics.Bitmap;

public class ServicePro {
		private int Id;
		private String Name;
		private String istrans;
		private String lang;
		private String code;
		
		public ServicePro(){
			
		}
		
		public ServicePro(int Id,String Name,String istrans,String lang,String code){
			this.Id = Id;
			this.Name = Name;
			this.istrans = istrans;
			this.lang = lang;
			this.code = code;
			
		}
		
		public void setId(int Id){
			this.Id=Id;
		}
		
		public void setName(String Name){
			this.Name = Name;
		}
		
		public void setLang(String Name){
			this.lang = Name;
		}
		
		public void setcode(String Name){
			this.code = Name;
		}
		
		public void setShowOnMap(String istrans){
			this.istrans = istrans;
		}
		
		
		
		public int getId(){
			return this.Id;
		}
		
		public String getName(){
			return this.Name ;
		}
		
		public String getLang(){
			return this.lang ;
		}

		public String getcode(){
			return this.code ;
		}
		
		public String getistrans(){
			return this.istrans ;
		}
		
	
		
		
		/* public String toString(){
			return "\nid= " + this.Id + "\nTitle= "+this.Name + "\nbody= "+this.Body + "\nDate= "+ this.Date + "\nthumbnailUrl= "+this.ThumbnailUrl + "\nImageUrl= "+this.ImageUrl + "\nTypeId= "+this.TypeId;
		}*/
	}


