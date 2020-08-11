package com.ids.ict.classes;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UrlParserHandler extends DefaultHandler {
	//This is the list which shall be populated while parsing the XML.
    private String tempVal;
 
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
    	
    }
 
    /**
     * This will be called every time parser encounter a value node
     * */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch, start, length).trim();
 
    }
    //Accessor for newsList object
    public boolean isUnique(){
        if(tempVal.equals("true")) return true;
        else return false;
    }
    public String getReply(){
    	return tempVal;
    }
}
