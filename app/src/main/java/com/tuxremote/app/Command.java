package com.tuxremote.app;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lheido on 03/06/14.
 */
public class Command {
    public String name = null;
    public String cmd = null;
    public String icon = null;
    public int res = R.drawable.ic_action_content_remove;
    private final static String SPLIT_CHAR = ":";

    /**
     * Constructor 1
     * @param name : String, command name
     * @param cmd  : String, command shell
     * @param icon : int, drawable ressource like "R.drawable.ressource", default close icon for icon = 0.
     */
    public Command(String name, String cmd, String icon){
        this.name = name;
        this.cmd = cmd;
        this.icon = icon;
    }

    public void setIconToView(Context context, ImageView view){
        if(this.icon != null){
            File filePath = context.getFileStreamPath(this.icon);
            Picasso.with(context).load(filePath).into(view);
        }else{
            Picasso.with(context).load(this.res).into(view);
        }
    }

    public static Command cmdClose(String hexaId){
        return new Command("close", "wmctrl -i -c "+hexaId, null);
    }

    public static ArrayList<Command> createCmdsList(ArrayList<String> list){
        ArrayList<Command> cmds = new ArrayList<Command>();
        for (int i = 0; i < list.size(); i++) {
            String[] item = list.get(i).split(SPLIT_CHAR);
            String name = item[0];
            String cmd = item[1];
            String icon = null;
            if(item.length == 3)
                icon = item[2];
            cmds.add(new Command(name, cmd, icon));
        }
        return cmds;
    }
}
