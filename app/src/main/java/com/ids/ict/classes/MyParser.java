	package com.ids.ict.classes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.ids.ict.activities.Event;
import com.ids.ict.activities.Notifications;


    //class for parsing all pages
public class MyParser {
	/*
	public static void ParseNews(AtomicReference<News[]> newsRef,InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;
			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			NewsParserHandler handler = new NewsParserHandler();

			//Create parser from factory
			//parser = XMLReaderFactory.createXMLReader();

			//Register handler with parser
			parser.setContentHandler(handler);
			//parse the data
			parser.parse(inSource);

			//populate the parsed News list in above created empty list; You can return from here also.
			ArrayList<News> newsList = new ArrayList<News>(handler.getNews());
			newsRef.set(newsList.toArray(new News[newsList.size()]));
			

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public static void ParseCategory(AtomicReference<Category[]> catRef,InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			CategoryParserHandler handler = new CategoryParserHandler();

			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);

			//populate the parsed Category list in above created empty list; You can return from here also.
			ArrayList<Category> catList = new ArrayList<Category>(handler.getCategory());
			catList.add(0, new Category(0,"All"));
			catRef.set(catList.toArray(new Category[catList.size()]));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	public static void ParseAlbum(AtomicReference<Album[]> albumRef,InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			AlbumParserHandler handler = new AlbumParserHandler();

			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);

			//populate the parsed Category list in above created empty list; You can return from here also.
			ArrayList<Album> albumsList = new ArrayList<Album>(handler.getAlbums());
			albumRef.set(albumsList.toArray(new Album[albumsList.size()]));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	*/
	public static void ParseEvent(AtomicReference<Event[]> eventRef, InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			EventParserHandler handler = new EventParserHandler();

			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);

			//populate the parsed event list in above created empty list; You can return from here also.

			ArrayList<Event> eventList = new ArrayList<Event>(handler.getEvents());
			eventRef.set(handler.getEvents().toArray(new Event[eventList.size()]));
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {

		}
	}
	
	public static void ParseNotification(AtomicReference<Notifications[]> eventRef, InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			NotificationParserHandler handler = new NotificationParserHandler();

			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);

			//populate the parsed event list in above created empty list; You can return from here also.
			
			
			ArrayList<Notifications> eventList = new ArrayList<Notifications>(handler.getEvents());
			eventRef.set(handler.getEvents().toArray(new Notifications[eventList.size()]));
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	/*
	public static void ParseMedia(AtomicReference<Media[]> mediaRef, InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();

			//Create default handler instance
			MediaParserHandler handler = new MediaParserHandler();

			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);

			//populate the parsed event list in above created empty list; You can return from here also.
			ArrayList<Media> mediaList = new ArrayList<Media>(handler.getMedia());
			mediaRef.set(handler.getMedia().toArray(new Media[mediaList.size()]));
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	*/
	public static boolean ParseUrl(InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;
		UrlParserHandler handler = new UrlParserHandler();

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();


			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return handler.isUnique();
	}
	public static String ParseReply(InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;
		UrlParserHandler handler = new UrlParserHandler();

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();


			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return handler.getReply();
	}
	
	public static String ParseAboutUs(InputSource inSource) throws ParserConfigurationException{
		XMLReader  parser;
		AboutUsHandler handler = new AboutUsHandler();

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware (true);
			parser = factory.newSAXParser ().getXMLReader ();


			//Register handler with parser
			parser.setContentHandler(handler);
			
			//parse the data
			parser.parse(inSource);			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return handler.getBody();
		
		
	}
	
	
	
	
	
}
