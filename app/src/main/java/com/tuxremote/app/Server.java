package com.tuxremote.app;

public class Server {
    private String name = null;
    private String ip = null;
    private String userId = null;
    private String password = null;
    private boolean available = false;

    /**
     * Constructor
     * @param n   : server name for friendly usage.
     * @param ad  : ip address.
     * @param uid : user id.
     * @param pw  : user password.
     */
    public Server(String n, String ad, String uid, String pw){
        name = n; ip = ad; userId = uid; password = pw;
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

    public void setPassword(String pw){
        password = pw;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
