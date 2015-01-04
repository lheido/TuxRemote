package com.tuxremote.app;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class Command {
    public String name = null;
    public String cmd = null;
    public String icon = null;

    /**
     * Constructor
     * @param name : String, command name.
     * @param cmd  : String, command shell.
     * @param icon : int, drawable ressource like "R.drawable.ressource", default close icon for icon = 0.
     */
    public Command(String name, String cmd, String icon){
        this.name = name;
        this.cmd = cmd;
        this.icon = icon;
    }

    public Command copy(){
        return new Command(
                ""+this.name, ""+this.cmd, ""+this.icon
        );
    }

    public void setIconToView(Context context, ImageView view){
        if(this.icon != null) {
            if (this.icon.equals("close")) {
                Picasso.with(context).load(TuxRemoteUtils.DEFAULT_CLOSE_RES).fit().centerInside().into(view);
            } else{
                File filePath = context.getFileStreamPath(this.icon);
                Picasso.with(context).load(filePath).fit().centerInside().into(view);
            }
        }
    }

    public String getName(){
        return this.name;
    }

    public static Command cmdClose(String hexaId){
        return new Command("close", "DISPLAY=:0 wmctrl -i -c "+hexaId, "close");
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
