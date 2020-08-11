package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.Satisfaction;


public class SatisfactionXMLPullParserHandler {
	
	ArrayList<Satisfaction> satisfactionList;
	
	    private Satisfaction satisfaction;
	    
	    private String text;
	 
	    public SatisfactionXMLPullParserHandler() {
	    	satisfactionList = new ArrayList<Satisfaction>();
	    	
	    }
	 
	    public ArrayList<Satisfaction> getCategoryList() {
	        return satisfactionList;
	    }
	   
	    public ArrayList<Satisfaction> parse(InputStream is) {
	        XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;
	        try {
	            factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	 
	            parser.setInput(is, null);
	 
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("SpeedTestSatisfaction")) {
	                        // create a new instance of employee
	                    	satisfaction = new Satisfaction();
	                    	
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("SpeedTestSatisfaction")) {
	                        // add news object to list
	                    	satisfactionList.add(satisfaction);
	                    	
	                    } else if (tagname.equalsIgnoreCase("Id")) {
	                    	satisfaction.setId(text.trim());
	                    	
	                    } else if (tagname.equalsIgnoreCase("Name")) {
	                    	satisfaction.setValue(text.trim());
	                    	
	                    }
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	      
	      return satisfactionList;
	    }
	}

