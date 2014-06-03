package com.tuxremote.app;

import java.util.Map;

/**
 * Created by lheido on 03/06/14.
 */
public class App {
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    private Command[] cmds = null;

    public App(String hexaId, String pid, String title, Command[] cmd){
        this.id = hexaId;
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
