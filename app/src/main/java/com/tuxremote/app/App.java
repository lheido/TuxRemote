package com.tuxremote.app;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lheido on 03/06/14.
 */
public class App {
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    private ArrayList<Command> cmds = new ArrayList<Command>();

    public App(String hexaId, String pid, String title, ArrayList<Command> cmds){
        this.id = hexaId;
        this.pid = pid;
        this.title = title;
        this.cmds.addAll(cmds);
        // add cmd close at the end of cmds list!
        this.cmds.add(Command.cmdClose(this.id));
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
