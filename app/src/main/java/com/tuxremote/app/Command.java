package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

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

    public void setIconToView(Context context, ImageView view){
        if(this.icon != null) {
            if (this.icon.equals("close")) {
                Picasso.with(context).load(TuxRemoteUtils.DEFAULT_CLOSE_RES).fit().centerInside().into(view);
            } else{
                File filePath = context.getFileStreamPath(this.icon);
                Picasso.with(context).load(filePath).fit().centerInside().into(view);
            }
        }
//        else{
//            Picasso.with(context).load(TuxRemoteUtils.DEFAULT_ICON_APP).fit().centerInside().into(view);
//        }
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<String> exec(){
//        return session.setCommand("exec", this.cmd);
        return new ArrayList<String>();
    }

    public static Command cmdClose(String hexaId){
        return new Command("close", "DISPLAY=:0 wmctrl -i -c "+hexaId, "close");
    }

    public static ArrayList<Command> createCmdsList(final ArrayList<String> list){
        ArrayList<Command> cmds = new ArrayList<Command>();
        for (String aList : list) {
            String[] item = aList.split(TuxRemoteUtils.SPLIT_CHAR);
            String name = item[0];
            String cmd = item[1];
            String icon = null;
            if (item.length == 3)
                icon = item[2];
            cmds.add(new Command(name, cmd, icon));
        }
        return cmds;
    }

    public static Command newCommand(final String cmdLineFromConfigFile){
        String[] item = cmdLineFromConfigFile.split(TuxRemoteUtils.SPLIT_CHAR);
        String name = item[0];
        String cmd = item[1];
        String icon = null;
        if (item.length == 3)
            icon = item[2];
        return new Command(name, cmd, icon);
    }

    public String getCmd() {
        return cmd;
    }

    public static abstract class ListCmdTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<App> listCmd = null;

        ListCmdTask(){
            listCmd = new ArrayList<App>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // create app liste with wmctrl
            return true;
        }

        @Override
        abstract protected void onPostExecute(Boolean result);

        public void execTask(){
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                execute();
            }
        }
    }

}
