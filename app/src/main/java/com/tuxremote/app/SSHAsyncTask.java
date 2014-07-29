package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class SSHAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final Command cmd;
    private WeakReference<MainActivity> act;
    private Context context;

    public SSHAsyncTask(MainActivity activity, Command cmd){
        link(activity);
        this.cmd = cmd;
    }

    private void link(MainActivity activity) {
        act = new WeakReference<MainActivity>(activity);
    }

    @Override
    protected void onPreExecute () {
        if(act.get() != null){
            context = act.get().getApplicationContext();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // isCancelled() //doc : the value returned by this method should be checked periodically here
        // call onProgressUpdate to publish updates on the UI thread.
        //publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate (Void... prog) {
        if (act.get() != null){
            //act.get().updateProgress(prog[0]);
        }
    }

    @Override
    protected void onPostExecute (Boolean result) {
        if (act.get() != null) {
            if(!result){
                // échec :(
            }
        }
    }
}
