package com.ids.ict.classes;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ids.ict.activities.Event;

import android.util.Log;

public class ArEventParserHandler extends DefaultHandler {
	
    private ArrayList<Event> eventsList = new ArrayList<Event>();
    private Event tempEvent;
    private String tempVal;
    private String tempVadesc;
    private boolean isnewid = false;
    
    public void startDocument() throws SAXException
    {
        System.out.println("start of the document   : ");
    }
 
    public void endDocument() throws SAXException
    {
        System.out.println("end of the document document     : ");
    }
 
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
    	//reset
    			tempVal = "";
    			tempVadesc="";
    			if(qName.equalsIgnoreCase("IssueType")) {
    				//create a new instance of News
    				tempEvent = new Event();
    				isnewid = true;
    			}
    				
    }
 
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
    	if(qName.equalsIgnoreCase("IssueType")) {
			//add it to the list
			eventsList.add(tempEvent);
			Log.d("description",tempEvent.getDescription());
			System.out.println(tempEvent);
		}else if (qName.equalsIgnoreCase("Id")) {
			if(isnewid){
			tempEvent.setId(Integer.parseInt(tempVal));
			isnewid = false;
			}
		}else if (qName.equalsIgnoreCase("Name")) {
			tempEvent.setName(tempVal);
		}else if (qName.equalsIgnoreCase("LocationMandatory")) {
			tempEvent.setdescription(tempVadesc);
		}else if (qName.equalsIgnoreCase("ServiceProviderTransfer")) {
			tempEvent.setDate(tempVal);
		}else if (qName.equalsIgnoreCase("AMLogo")) {
			tempEvent.setImageUrlDay(tempVal);
			tempEvent.setThumbnailUrlDay(tempVal);
		}else if (qName.equalsIgnoreCase("PMLogo")) {
			tempEvent.setImageUrlNight(tempVal);
			tempEvent.setThumbnailUrlNight(tempVal);
		}else if (qName.equalsIgnoreCase("Roaming")) {
			tempEvent.setLocation(tempVal);
		}
    	
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal =  new String(ch, start, length);//.replaceAll("\\<.*?\\>", "").trim();
        tempVadesc = tempVadesc + new String(ch, start, length);//.replaceAll("\\<.*?\\>", "").trim();
 
    }

    public ArrayList<Event> getEvents()
    {
        return eventsList;
    }
}
