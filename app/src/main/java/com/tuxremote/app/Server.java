package com.tuxremote.app;

public class Server {
    private String name;
    private String ip;
    private String userId;

    /**
     * Constructor
     * @param n   : server name for friendly usage.
     * @param ad  : ip address.
     * @param uid : user id.
     */
    public Server(String n, String ad, String uid){
        name = n; ip = ad; userId = uid;
    }

    public void setName(String n){
        name = n;
    }

    public String getName() {
        return name;
    }

    public void setIp(String ad){
        ip = ad;
    }

    public String getIp() {
        return ip;
    }

    public void setUserId(String n){
        userId = n;
    }

    public String getUserId(){
        return userId;
    }
}
