package com.ids.ict.classes;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ids.ict.activities.Event;
import com.ids.ict.activities.Notifications;

import android.util.Log;

public class NotificationParserHandler extends DefaultHandler {
	//This is the list which shall be populated while parsing the XML.
    private ArrayList<Notifications> eventsList = new ArrayList<Notifications>();
    private Notifications tempEvent;
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
    			if(qName.equalsIgnoreCase("Notification")) {
    				//create a new instance of News
    				tempEvent = new Notifications();
    				isnewid = true;
    			}
    				
    }
 
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
    	if(qName.equalsIgnoreCase("Notification")) {
			//add it to the list
			eventsList.add(tempEvent);
			Log.d("description",tempEvent.getDescription());
			System.out.println(tempEvent);
		}else if (qName.equalsIgnoreCase("Id")) {
			if(isnewid){
			tempEvent.setId(Integer.parseInt(tempVal));
			isnewid = false;
			}
		}else if (qName.equalsIgnoreCase("Title")) {
			tempEvent.setName(tempVal);
		}else if (qName.equalsIgnoreCase("Details")) {
			tempEvent.setdescription(tempVadesc);
		}else if (qName.equalsIgnoreCase("Date")) {
			tempEvent.setDate(tempVal);
		}
    	
    }
 
    /**
     * This will be called everytime parser encounter a value node
     * */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal =  new String(ch, start, length);//.replaceAll("\\<.*?\\>", "").trim();
        tempVadesc = tempVadesc + new String(ch, start, length);//.replaceAll("\\<.*?\\>", "").trim();
 
    }
    //Accessor for newsList object
    public ArrayList<Notifications> getEvents()
    {
        return eventsList;
    }
}
