package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.MobileConfiguration;

public class GetMobileConfiguration {
	ArrayList<MobileConfiguration> configList;
	
    private MobileConfiguration mobileConfig;
    
    private String text;
 
    public GetMobileConfiguration() {
    	configList = new ArrayList<MobileConfiguration>();
    	
    }
 
    public ArrayList<MobileConfiguration> getMobileConfigList() {
        return configList;
    }
   
    public ArrayList<MobileConfiguration> parse(InputStream is) {
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
                    if (tagname.equalsIgnoreCase("MobileConfiguration")) {
                       
                    	mobileConfig = new MobileConfiguration();
                    	
                    }
                    break;
 
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
 
                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("MobileConfiguration")) {
                        // add news object to list
                    	configList.add(mobileConfig);
                    	
                    } else if (tagname.equalsIgnoreCase("Id")) {
                    	mobileConfig.setId(text.trim());
                    	
                    } else if (tagname.equalsIgnoreCase("Key")) {
                    	mobileConfig.setkey(text.trim());
                    	
                    }else if (tagname.equalsIgnoreCase("Value")) {
                    	mobileConfig.setValue(text.trim());
                    	
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
      
      return configList;
    }
}

