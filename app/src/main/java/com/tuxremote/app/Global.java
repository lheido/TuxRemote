package com.tuxremote.app;

public class Global {
    private static boolean connexion = false;
    public static boolean userIsConnected(){
        return connexion;
    }
    public static void setUserIsConnected(boolean c){
        connexion = c;
    }
}
