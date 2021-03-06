package com.tuxremote.app;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class App {
    private String wmctrlName = null;
    private String name = null;
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    private String icon = null;
    private boolean staticApp = false;

    /**
     * Constructor
     * @param hexaId : wmctrl id in hexa.
     * @param pid    : pid.
     * @param name   : app name
     * @param title  : window title.
     * @param icon   : app icon.
     */
    public App(String hexaId, String pid, String name, String title, String icon){
        this.id = hexaId;
        this.pid = pid;
        this.name = name;
        this.title = title;
        this.icon = icon;
    }

    public App(String hexaId, String pid, String wmctrlName, String title){
        this.id = hexaId;
        this.pid = pid;
        this.wmctrlName = wmctrlName;
        this.title = title;
    }

    public App(String name, String wmctrl_name, String icon){
        this.name = name;
        this.wmctrlName = wmctrl_name;
        this.icon = icon;
    }

    public void setIconToView(Context context, ImageView view){
        if(this.icon != null){
            try {
                File filePath = context.getFileStreamPath(this.icon);
                Picasso.with(context).load(this.icon).fit().centerCrop().into(view);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//        else{
//            Picasso.with(context).load(TuxRemoteUtils.DEFAULT_ICON_APP).fit().centerCrop().into(view);
//        }
    }

    /**
     * use to merge App from wmctrl with config.xml
     * @param app
     */
    public void merge(App app){
        this.name = app.getName();
        if(app != null)
            this.icon = app.icon;
    }

    public String getHexaId(){
        return this.id;
    }

    public String getPid(){
        return this.pid;
    }

    public String getTitle(){
        return this.title;
    }
    public String getName(){
        return this.name;
    }

    public void setHexaId(String id){
        this.id = id;
    }

    public void setPid(String pid){
        this.pid = pid;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setName(String n){
        this.name = n;
    }

    public String getWmctrlName() {
        return wmctrlName;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isStaticApp() {
        return staticApp;
    }

    public void setStaticApp(boolean staticApp) {
        this.staticApp = staticApp;
    }
}
