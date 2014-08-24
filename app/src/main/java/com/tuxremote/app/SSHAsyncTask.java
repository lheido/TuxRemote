package com.tuxremote.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

public class SSHAsyncTask extends AsyncTask<Void, BashReturn, Boolean> {

    private final Command cmd;
    private Context context;

    public SSHAsyncTask(Command cmd){
        this.cmd = cmd;
    }

    @Override
    protected void onPreExecute () {
        context = Global.getStaticContext();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            if (!cmd.getCmd().contains("&")) {
                BashReturn retour = Global.session.SetCommand(cmd.getCmd());
                if (retour != null) {
                    publishProgress(retour);
                }
            } else {
                Global.session.setCommandNoReturn(cmd.getCmd());
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
