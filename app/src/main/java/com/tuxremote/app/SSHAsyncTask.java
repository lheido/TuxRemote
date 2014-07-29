package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

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
        try {
            BashReturn retour = Global.session.SetCommand(cmd.getCmd());
            if(retour != null)
                Log.v("DoInBackground", ""+retour.getBashReturn().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
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

    }
}
