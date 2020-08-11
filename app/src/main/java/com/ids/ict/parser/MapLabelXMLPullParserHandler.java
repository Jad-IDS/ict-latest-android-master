package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.Provider;
import com.ids.ict.classes.Satisfaction;


public class MapLabelXMLPullParserHandler {
	
	ArrayList<MapLabel> mapLabelList;
	
	    private MapLabel mapLabel;
	    
	    private String text;
	 
	    public MapLabelXMLPullParserHandler() {
	    	mapLabelList = new ArrayList<MapLabel>();
	    	
	    }
	 
	    public ArrayList<MapLabel> getMapLabelList() {
	        return mapLabelList;
	    }
	   
	    public ArrayList<MapLabel> parse(InputStream is) {
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
	                    if (tagname.equalsIgnoreCase("MapLabel")) {
	                       
	                    	mapLabel = new MapLabel();
	                    	
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("MapLabel")) {
	                        // add news object to list
	                    	mapLabelList.add(mapLabel);
	                    	
	                    } else if (tagname.equalsIgnoreCase("Description")) {
	                    	mapLabel.setLabel(text.trim());
	                    	
	                    } else if (tagname.equalsIgnoreCase("PinIcon")) {
	                    	mapLabel.setImageName(text.trim());
	                    	
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
	      
	      return mapLabelList;
	    }
	}

