package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.Provider;


public class SpeedTestSPParser {

	ArrayList<Provider> providerList;

	private Provider provider;

	private String text;

	public SpeedTestSPParser(String name) {
		providerList = new ArrayList<Provider>();
		Provider p = new Provider(name);
		p.setId("0");
		p.setIsForTransfer("true");
		providerList.add(p);

	}

	public ArrayList<Provider> getProviderList() {
		return providerList;
	}

	public ArrayList<Provider> parse(InputStream is) {
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
					if (tagname.equalsIgnoreCase("ServiceProvider")) {

						provider = new Provider();

					}
					break;

				case XmlPullParser.TEXT:
					text = parser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagname.equalsIgnoreCase("ServiceProvider")) {
						// add news object to list
						if(provider.getIsForTransfer().equalsIgnoreCase("false"))
							providerList.add(provider);

					} else if (tagname.equalsIgnoreCase("Id")) {
						provider.setId(text.trim());

					} else if (tagname.equalsIgnoreCase("Name")) {
						provider.setName(text.trim());

					}else if (tagname.equalsIgnoreCase("IsForTransfer")) {
						provider.setIsForTransfer(text.trim());

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

		return providerList;
	}
}

