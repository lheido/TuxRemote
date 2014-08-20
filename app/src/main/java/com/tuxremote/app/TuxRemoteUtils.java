package com.tuxremote.app;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TuxRemoteUtils {

//    public final static String CMD_RESTART  = "dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager org.freedesktop.ConsoleKit.Manager.Restart";
//    public final static String CMD_SHUTDOWN = "dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager  org.freedesktop.ConsoleKit.Manager.Stop";
    public final static String CMD_SHUTDOWN = "sudo $HOME/.config/TuxRemote/TuxRemote-shutdown";
    public final static String ICONS_PATH = "~/.config/TuxRemote/icons/";
    public final static int DEFAULT_CLOSE_RES = R.drawable.ic_action_content_remove;
    public final static int DEFAULT_ICON_APP = R.drawable.ic_launcher;
    public static final String SERVERS_LIST = "servers_list";
    public static final String PREF_SPLIT = "°SPLIT°°";
    public static final String CONFIG_FILE = "config.xml";
    public static final String CMD_WMCTRL = "DISPLAY=:0 wmctrl -lpx | awk  'BEGIN{FS=\" \"} NF>4 && $4!=\"N/A\"{printf $1\" \" $3\" \"$4\" \" ;for(i=6; i<=NF; i++){printf $i\" \"}; printf \"\\n\"} '";

    /**
     * Write image to internal storage
     * @param context  : Context android.
     * @param fileName : Image file name.
     * @param image    : Bitmap image format PNG only.
     */
    public static void saveImageToInternalStorage(Context context, String fileName, Bitmap image){
        // Convert Bitmap to byteArray
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Open fileOutput with fileName and write byteArray
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            if(fos != null) {
                fos.write(byteArray);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write content to file fileName in internal storage
     * @param context  : context.
     * @param fileName : file name.
     * @param content  : content to back up.
     */
    public static void saveFileToInternalStorage(Context context, String fileName, String content){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static abstract class TuxRemoteDialog extends Dialog{
        /**
         * Custom dialog box
         * @param context   : Context android.
         * @param ressource : layout.
         * @param title     : dialog title.
         */
        public TuxRemoteDialog(Context context, int ressource, String title) {
            super(context);
            setContentView(ressource);
            setTitle(title);
            customInit();
            Button ok = (Button) findViewById(R.id.ok_button);
            if(ok != null){
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customOk();
                        dismiss();
                    }
                });
            }
            Button cancel = (Button) findViewById(R.id.cancel_button);
            if(cancel != null){
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customCancel();
                        cancel();
                    }
                });
            }
        }

        public abstract void customInit();

        public abstract void customCancel();

        public abstract void customOk();
    }

    public static App getAppFromWmctrlLine(String line){
        String[] splited = line.split(" ", 4);
        return new App(splited[0], splited[1], splited[2], (splited.length == 4)? splited[3]: "");
    }

    public static ArrayList<App> getAppListFromWmctrl(ArrayList<String> retour){
        ArrayList<App> appList = new ArrayList<App>();
        for (String line : retour)
            appList.add(getAppFromWmctrlLine(line));
        return appList;
    }

    public static SharedPreferences getPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static class scpTask extends AsyncTask<Void, Void, Void>{

        private final String fileName;

        public scpTask(String fileName){
            this.fileName = fileName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Global.session.scpImage(Global.getStaticContext(), fileName);
            return null;
        }

        public void execTask() {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                execute();
            }
        }
    }
}
