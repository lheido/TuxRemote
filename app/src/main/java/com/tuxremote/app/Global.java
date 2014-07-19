package com.tuxremote.app;

/**
 * Created by ubuntu on 19/07/14.
 */
public class Global {
    private static boolean connexion = false;
    public static boolean userIsConnected(){
        return connexion;
    }
    public static void setUserIsConnected(boolean c){
        connexion = c;
    }
}
