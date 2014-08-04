package com.tuxremote.app;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigXML {
    private XmlPullParser xpp = null;
    private XmlPullParserFactory factory = null;

    public ConfigXML(Context context){
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(context.openFileInput(TuxRemoteUtils.CONFIG_FILE), null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<App> getAppList(){
        ArrayList<App> appList = new ArrayList<App>();
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG: break;
                    case XmlPullParser.END_TAG: break;
                    case XmlPullParser.TEXT: break;
                }
                eventType = xpp.next();
//              System.out.println("Start tag "+xpp.getName());
//              System.out.println("End tag "+xpp.getName());
//              System.out.println("Text "+xpp.getText());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appList;
    }
}
