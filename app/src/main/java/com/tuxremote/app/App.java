package com.tuxremote.app;

/**
 * Created by lheido on 03/06/14.
 */
public class App {
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    public App(){}
    public App(String id, String pid, String title){
        this.id = id;
        this.pid = pid;
        this.title = title;
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
    public void setHexaId(String id){
        this.id = id;
    }
    public void setPid(String pid){
        this.pid = pid;
    }
    public void setTitle(String title){
        this.title = title;
    }
}
