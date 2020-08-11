package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.MapPinLocation;
import com.ids.ict.classes.Provider;
import com.ids.ict.classes.Satisfaction;


public class MapPinLocationXMLPullParserHandler {
	
	ArrayList<MapPinLocation> mapPinLocationList;
	
	    private MapPinLocation mapPinLocation;
	    
	    private String text;
	 
	    public MapPinLocationXMLPullParserHandler() {
	    	mapPinLocationList = new ArrayList<MapPinLocation>();
	    	
	    }
	 
	    public ArrayList<MapPinLocation> getMapLabelList() {
	        return mapPinLocationList;
	    }
	   
	    public ArrayList<MapPinLocation> parse(InputStream is) {
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
	                    if (tagname.equalsIgnoreCase("MapPin")) {
	                       
	                    	mapPinLocation = new MapPinLocation();
	                    	
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("MapPin")) {
	                        // add news object to list
	                    	mapPinLocationList.add(mapPinLocation);
	                    	
	                    } else if (tagname.equalsIgnoreCase("Locality")) {
	                    	mapPinLocation.setLocality(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("SubLocality")) {
							mapPinLocation.setSubLocality(text.trim());

						}
						else if (tagname.equalsIgnoreCase("SubLocalityAr")) {
							mapPinLocation.setSubLocalityAr(text.trim());

						}
						else if (tagname.equalsIgnoreCase("IssueTypeAr")) {
							mapPinLocation.setIssueTypeAr(text.trim());

						}
						else if (tagname.equalsIgnoreCase("LocationX")) {
	                    	mapPinLocation.setLocationX(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("LocationY")) {
	                    	mapPinLocation.setLocationY(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("IssueType")) {
	                    	mapPinLocation.setIssueType(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("Number")) {
	                    	mapPinLocation.setNumber(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("PinIcon")) {
	                    	mapPinLocation.setPinIcon(text.trim());
	                    	
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
	      
	      return mapPinLocationList;
	    }
	}

