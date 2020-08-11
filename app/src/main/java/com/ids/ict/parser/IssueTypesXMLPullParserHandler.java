package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R;
import android.content.Context;

import com.ids.ict.classes.IssueTypes;
import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.Provider;
import com.ids.ict.classes.Satisfaction;


public class IssueTypesXMLPullParserHandler {
	
	ArrayList<IssueTypes> issueTypeslList;
	
	    private IssueTypes issueTypes;
	    
	    private String text;
	 private Context cc;
	    public IssueTypesXMLPullParserHandler(Context c,String name) {
	    	issueTypeslList = new ArrayList<IssueTypes>();
	    	cc=c;
	    	IssueTypes issue = new IssueTypes(name);
	    	issue.setId("0");
	    	issue.setShowOnMap(true);
	    	issueTypeslList.add(issue);
	    	
	    }
	 
	    public ArrayList<IssueTypes> getMapLabelList() {
	        return issueTypeslList;
	    }
	   
	    public ArrayList<IssueTypes> parse(InputStream is) {
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
	                    if (tagname.equalsIgnoreCase("IssueType")) {
	                       
	                    	issueTypes = new IssueTypes();
	                    	
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("IssueType")) {
	                        // add news object to list
	                   if(issueTypes.isShowOnMap())
	                    	issueTypeslList.add(issueTypes);
	                    	
	                    } else if (tagname.equalsIgnoreCase("Id")) {
	                    	issueTypes.setId(text.trim());
	                    	
	                    } else if (tagname.equalsIgnoreCase("Name")) {
	                    	issueTypes.setName(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("ShowOnMap")) {
	                    	if(text.trim().equals("true"))
	                    	issueTypes.setShowOnMap(true);
	                    	else
	                    		issueTypes.setShowOnMap(false);
	                    	
	                    }else if (tagname.equalsIgnoreCase("LocationMandatory")) {
	                    	issueTypes.setLocationMandatory(Boolean.getBoolean(text.trim()));
	                    	
	                    }else if (tagname.equalsIgnoreCase("ServiceProviderTransfer")) {
	                    	issueTypes.setServiceProviderTransfer(Boolean.getBoolean(text.trim()));
	                    	
	                    }else if (tagname.equalsIgnoreCase("Roaming")) {
	                    	issueTypes.setRoaming(Boolean.getBoolean(text.trim()));
	                    	
	                    }else if (tagname.equalsIgnoreCase("Logo")) {
	                    	issueTypes.setLogo(text.trim());
	                    	
	                    }else if (tagname.equalsIgnoreCase("Description")) {
	                    	issueTypes.setMainIssueId(Boolean.getBoolean(text.trim()));
	                    	
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
	      
	      return issueTypeslList;
	    }
	}

