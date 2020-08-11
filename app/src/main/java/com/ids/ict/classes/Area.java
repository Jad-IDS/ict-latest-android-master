package com.ids.ict.classes;

import java.util.ArrayList;

/**
 * Created by Amal on 10/18/2016.
 */
public class Area {

    private int Id;
    private String nameAr,nameEn;
private ArrayList<SubArea> subAreas;
    public Area()
    {

    }

    public int getId() {
        return Id;
    }

    public ArrayList<SubArea> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(ArrayList<SubArea> subAreas) {
        this.subAreas = subAreas;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
