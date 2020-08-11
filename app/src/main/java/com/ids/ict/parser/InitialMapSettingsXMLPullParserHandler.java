package com.ids.ict.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.MapLabel;
import com.ids.ict.classes.Provider;
import com.ids.ict.classes.Satisfaction;

public class InitialMapSettingsXMLPullParserHandler {

	ArrayList<InitialMapSettings> initialMapSettingsList;

	private InitialMapSettings initialMapSettings;

	private String text;

	public InitialMapSettingsXMLPullParserHandler() {
		initialMapSettingsList = new ArrayList<InitialMapSettings>();

	}

	public ArrayList<InitialMapSettings> getInitialMapSettingsList() {
		return initialMapSettingsList;
	}

	public ArrayList<InitialMapSettings> parse(InputStream is) {
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
					if (tagname.equalsIgnoreCase("InitialMapSetting")) {

						initialMapSettings = new InitialMapSettings();

					}
					break;

				case XmlPullParser.TEXT:
					text = parser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagname.equalsIgnoreCase("InitialMapSetting")) {
						// add news object to list
						initialMapSettingsList.add(initialMapSettings);

					} else if (tagname.equalsIgnoreCase("LocationX")) {
						initialMapSettings.setLocationX(text.trim());

					} else if (tagname.equalsIgnoreCase("LocationY")) {
						initialMapSettings.setLocationY(text.trim());

					} else if (tagname.equalsIgnoreCase("ZoomSetting")) {
						initialMapSettings.setZoomSettings(text.trim());

					} else if (tagname.equalsIgnoreCase("Threshold")) {
						initialMapSettings.setThreshold(text.trim());

					} else if (tagname.equalsIgnoreCase("ShowServiceProviders")) {
						initialMapSettings.setShowServiceProvider(Boolean
								.getBoolean(text.trim()));

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return initialMapSettingsList;
	}
}
