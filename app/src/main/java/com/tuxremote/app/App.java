package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lheido on 03/06/14.
 */
public class App {
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    private String icon = null;
    private ArrayList<Command> cmds = new ArrayList<Command>();

    public App(String hexaId, String pid, String title, String icon, ArrayList<Command> cmds){
        this.id = hexaId;
        this.pid = pid;
        this.title = title;
        this.icon = icon;
        if(cmds != null)
            this.cmds.addAll(cmds);
        // add cmd close at the end of cmds list!
        this.cmds.add(Command.cmdClose(this.id));
    }
    public void setIconToView(Context context, ImageView view){
        if(this.icon != null){
            File filePath = context.getFileStreamPath(this.icon);
            Picasso.with(context).load(filePath).resize(view.getWidth(), view.getHeight()).into(view);
        }else{
            Picasso.with(context).load(TuxRemoteUtils.DEFAULT_ICON_APP).resize(view.getWidth(), view.getHeight()).into(view);
        }
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
    public ArrayList<Command> getCmds(){
        return this.cmds;
    }

    public static abstract class ListAppTask extends AsyncTask<Void, Void, Boolean>{

        private ArrayList<App> listApp = null;

        ListAppTask(){
            listApp = new ArrayList<App>();
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
