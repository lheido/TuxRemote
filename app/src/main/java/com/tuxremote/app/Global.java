package com.tuxremote.app;

public class Global {
    private static boolean connexion = true;

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
}
