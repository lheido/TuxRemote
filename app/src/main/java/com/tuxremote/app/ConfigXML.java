package com.tuxremote.app;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigXML {
    private static final String APPLICATION = "Application";
    private static final String APP_NAME = "name";
    private static final String APP_WMCTRL_NAME = "wmctrl_name";
    private static final String APP_ICON = "icon";
    private static final String CMD_NAME = "name";
    private static final String CMD_SHELL = "cmd";
    private static final String CMD_ICON = "icon";
    private static final String COMMAND = "Command";
    private final Context context;
    private XmlPullParser xpp = null;
    private XmlPullParserFactory factory = null;

    public ConfigXML(Context context){
        this.context = context;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public App getApp(){
        HashMap<String, String> attrs = new HashMap<String, String>();
        int count = xpp.getAttributeCount();
        for (int i = 0; i < count; i++) {
            attrs.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
        }
        return new App(
            attrs.containsKey(APP_NAME)? attrs.get(APP_NAME) : null,
            attrs.containsKey(APP_WMCTRL_NAME)? attrs.get(APP_WMCTRL_NAME) : null,
            attrs.containsKey(APP_ICON)? attrs.get(APP_ICON) : null
        );
    }

    public Command getCommand(){
        HashMap<String, String> attrs = new HashMap<String, String>();
        int count = xpp.getAttributeCount();
        for (int i = 0; i < count; i++) {
            attrs.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
        }
        return new Command(
                attrs.containsKey(CMD_NAME)? attrs.get(CMD_NAME) : null,
                attrs.containsKey(CMD_SHELL)? attrs.get(CMD_SHELL) : null,
                attrs.containsKey(CMD_ICON)? attrs.get(CMD_ICON) : null
        );
    }

    public ArrayList<App> getAppList(){
        ArrayList<App> appList = new ArrayList<App>();
        int eventType = 0;
        try {
            xpp.setInput(context.openFileInput(TuxRemoteUtils.CONFIG_FILE), null);
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals(APPLICATION)){
                            appList.add(getApp());
                        }
                        break;
                    case XmlPullParser.END_TAG: break;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appList;
    }

    public ArrayList<Command> getCommandList(String appName){
        ArrayList<Command> cmdList = new ArrayList<Command>();
        App currentApp = null;
        int eventType = 0;
        try {
            xpp.setInput(context.openFileInput(TuxRemoteUtils.CONFIG_FILE), null);
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals(APPLICATION)){
                            currentApp = getApp();
                        }else if(currentApp != null && currentApp.getName().equals(appName) && xpp.getName().equals(COMMAND)){
                            cmdList.add(getCommand());
                        }
                        break;
                    case XmlPullParser.END_TAG: break;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmdList;
    }

    public App getApplication(String appName){
        App app = null;
        int eventType = 0;
        try {
            xpp.setInput(context.openFileInput(TuxRemoteUtils.CONFIG_FILE), null);
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals(APPLICATION)) {
                            App tmp = getApp();
                            if (getApp().getName().equals(appName))
                                app = tmp;
                        }
                        break;
                    case XmlPullParser.END_TAG: break;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return app;
    }

    public HashMap<String, String> getStaticCommand(String name){
        HashMap<String, String> cmds = new HashMap<String, String>();
        int eventType = 0;
        try {
            xpp.setInput(context.openFileInput(TuxRemoteUtils.CONFIG_FILE), null);
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals(name)) {
                            int count = xpp.getAttributeCount();
                            for (int i = 0; i < count; i++) {
                                cmds.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG: break;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmds;
    }
}
