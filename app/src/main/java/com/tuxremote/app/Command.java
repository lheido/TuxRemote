package com.tuxremote.app;

/**
 * Created by lheido on 03/06/14.
 */
public class Command {
    public String name = null;
    public String cmd = null;
    public int res = R.drawable.ic_action_content_remove;

    /**
     * Constructor 1
     * @param name : String, command name
     * @param cmd  : String, command shell
     * @param icon : int, drawable ressource like "R.drawable.ressource", default close icon for icon = 0.
     */
    public Command(String name, String cmd, int icon){
        this.name = name;
        this.cmd = cmd;
        if(icon != 0){
            this.res = icon;
        }
    }
}
