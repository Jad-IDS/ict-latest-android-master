package com.ids.ict.classes;



	import java.util.ArrayList;

	import org.xml.sax.Attributes;
	import org.xml.sax.SAXException;
	import org.xml.sax.helpers.DefaultHandler;

	import android.util.Log;

	public class AboutUsHandler extends DefaultHandler {
		//This is the list which shall be populated while parsing the XML.
	    private String tempVal,body;
	 
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
	    
	    				
	    }
	 
	    public void endElement(String uri, String localName, String qName) throws SAXException
	    {
	    	if(qName.equalsIgnoreCase("string")) {
				//add it to the list;
				Log.d("body",tempVal);
				body = tempVal;
			}
	    }
	 
	    /**
	     * This will be called everytime parser encounter a value node
	     * */
	    public void characters(char[] ch, int start, int length) throws SAXException
	    {
	        tempVal = new String(ch, start, length).trim();
	 
	    }
	    //Accessor for newsList object
	    public String getBody()
	    {
	        return body;
	    }
	}

	

