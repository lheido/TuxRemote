package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

import java.lang.ref.WeakReference;

public class SSHAsyncTask extends AsyncTask<Void, BashReturn, Boolean> {

    private final Command cmd;
//    private WeakReference<MainActivity> act;
    private Context context;

    public SSHAsyncTask(Command cmd){
//        link(activity);
        this.cmd = cmd;
    }

//    private void link(MainActivity activity) {
//        act = new WeakReference<MainActivity>(activity);
//    }

    @Override
    protected void onPreExecute () {
        context = Global.getStaticContext();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            BashReturn retour = Global.session.SetCommand(cmd.getCmd());
            if(retour != null) {
                Log.v("DoInBackground", "" + retour.getBashReturn().toString());
                publishProgress(retour);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Erreur cmd "+cmd.getName(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onProgressUpdate (BashReturn... prog) {}

    @Override
    protected void onPostExecute (Boolean result) {}

    public void execTask() {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            execute();
        }
    }
}
