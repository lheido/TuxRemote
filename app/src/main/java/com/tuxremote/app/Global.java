package com.tuxremote.app;

import android.content.Context;

import com.tuxremote.app.TuxeRemoteSsh.SshSession;

public class Global {
    private static boolean connexion = false;

    /**
     * userIsConnected : true or false if user is connected/disconneted to a server.
     * use setUserIsConnected to change value.
     * @return true/false
     */
    public static boolean userIsConnected(){
        return connexion;
    }
    public static void setUserIsConnected(boolean c){
        connexion = c;
    }

    private static Context context;

    public static void setContext(Context c){
        context = c;
    }

    public static Context getStaticContext(){
        return context;
    }

    public static SshSession session;

}
