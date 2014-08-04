package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class App {
    private String wmctrlName = null;
    private String name = null;
    private String id = null; // wmctrl identifier in hexadecimal.
    private String pid = null;
    private String title = null;
    private String icon = null;

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

    public App(String name, String wmctrl_name, String icon){
        this.name = name;
        this.wmctrlName = wmctrl_name;
        this.icon = icon;
    }

    public void setIconToView(Context context, ImageView view){
        if(this.icon != null){
            File filePath = context.getFileStreamPath(this.icon);
            Picasso.with(context).load(filePath).fit().centerCrop().into(view);
        }else{
            Picasso.with(context).load(TuxRemoteUtils.DEFAULT_ICON_APP).fit().centerCrop().into(view);
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

    public static abstract class ListAppTask extends AsyncTask<Void, Void, Boolean>{

        protected ArrayList<App> _listApp = null;

        ListAppTask(){
            _listApp = new ArrayList<App>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // create app liste with wmctrl -lpx
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
