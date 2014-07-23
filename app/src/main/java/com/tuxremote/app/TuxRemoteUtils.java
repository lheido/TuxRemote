package com.tuxremote.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class TuxRemoteUtils {

    public final static String SPLIT_CHAR = ":";
    public final static int DEFAULT_CLOSE_RES = R.drawable.ic_action_content_remove;
    public final static int DEFAULT_ICON_APP = R.drawable.ic_launcher;
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

    public static void saveFileToInternalStorage(Context context, String fileName, String content){

    }

    public final static String CMD_RESTART  = "dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager org.freedesktop.ConsoleKit.Manager.Restart";
    public final static String CMD_SHUTDOWN = "dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager  org.freedesktop.ConsoleKit.Manager.Stop";

    public static abstract class TuxRemoteDialog extends Dialog{

        public TuxRemoteDialog(Context context, int ressource, String title) {
            super(context);
            setContentView(ressource);
            setTitle(title);
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
        }

        public abstract void customOk();
    }

}
