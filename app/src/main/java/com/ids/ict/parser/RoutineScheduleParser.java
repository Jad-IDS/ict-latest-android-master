package com.ids.ict.parser;

import com.ids.ict.classes.RoutineSchedule;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Amal on 11/16/2017.
 */

public class RoutineScheduleParser {

    private RoutineSchedule routineSchedule = new RoutineSchedule();

    private String text;

    public RoutineScheduleParser() {
    }

    public RoutineSchedule getRoutineSchedule() {
        return routineSchedule;
    }

    public RoutineSchedule parse(InputStream is) {
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
                        if (tagname.equalsIgnoreCase("DailyRoutineSchedule ")) {
                            routineSchedule = new RoutineSchedule();

                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (tagname.equalsIgnoreCase("Id")) {

                            try {
                                routineSchedule.setId(Integer.parseInt(text.trim()));
                            } catch (Exception e) {
                                e.printStackTrace();

                                routineSchedule.setId(0);
                            }

                        } else if (tagname.equalsIgnoreCase("Frequency")) {

                            try {
                                routineSchedule.setFrequency(Integer.parseInt(text.trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                routineSchedule.setFrequency(10);
                            }

                        }else if (tagname.equalsIgnoreCase("Enabled")) {

                            try {
                                routineSchedule.setEnabled(text.trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                                routineSchedule.setEnabled("false");
                            }
                        }else if (tagname.equalsIgnoreCase("StartingTime")) {

                            try {
                                routineSchedule.setStartingTime(text.trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                                routineSchedule.setStartingTime(new SimpleDateFormat("yyyy-dd-M'T'HH:mm:ss", Locale.ENGLISH).format(new Date()));
                            }
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

        return routineSchedule;
    }
}


