package com.ids.ict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.ids.ict.activities.Event;
import com.ids.ict.classes.EventParserHandler;
import com.ids.ict.classes.UrlParserHandler;

//class for parsing all pages
public class MyParser {
	public static void ParseEvent(AtomicReference<Event[]> eventRef,
			InputSource inSource) throws ParserConfigurationException {
		XMLReader parser;

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver",
					"org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newSAXParser().getXMLReader();

			// Create default handler instance
			EventParserHandler handler = new EventParserHandler();

			// Register handler with parser
			parser.setContentHandler(handler);

			// parse the data
			parser.parse(inSource);

			// populate the parsed event list in above created empty list; You
			// can return from here also.

			ArrayList<Event> eventList = new ArrayList<Event>(
					handler.getEvents());
			eventRef.set(handler.getEvents().toArray(
					new Event[eventList.size()]));

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public static boolean ParseUrl(InputSource inSource)
			throws ParserConfigurationException {
		XMLReader parser;
		UrlParserHandler handler = new UrlParserHandler();

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver",
					"org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newSAXParser().getXMLReader();

			// Register handler with parser
			parser.setContentHandler(handler);

			// parse the data
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

	public static String ParseReply(InputSource inSource)
			throws ParserConfigurationException {
		XMLReader parser;
		UrlParserHandler handler = new UrlParserHandler();

		try {
			SAXParserFactory factory;

			System.setProperty("org.xml.sax.driver",
					"org.xmlpull.v1.sax2.Driver");

			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newSAXParser().getXMLReader();

			// Register handler with parser
			parser.setContentHandler(handler);

			// parse the data
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

}
