package com.ids.ict.classes;

   public class ICTLan{
	private String id;
    private String titleAr;
    private String titleEn;
    public ICTLan(){
	id="";
	titleAr="";
	titleEn="";
}
    public ICTLan(String id, String titleAr, String titleEn){
	this.id=id;
	this.titleAr=titleAr;
	this.titleEn=titleEn;
}
   public String getId() {
	return id;
}
   public void setId(String id) {
	this.id = id;
}
   public String getTitle(String lang) {
	if(lang.equals("ar"))
	return titleAr;
	else
		return titleEn;
}
   public void setTitleAr(String titleAr) {
	this.titleAr = titleAr;
}
   public void setTitleEn(String titleEn) {
	this.titleEn = titleEn;
}


}
