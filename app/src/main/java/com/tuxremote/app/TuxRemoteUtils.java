package com.tuxremote.app;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class TuxRemoteUtils {

    public final static String SPLIT_CHAR = ":";
    public final static int DEFAULT_CLOSE_RES = R.drawable.ic_action_content_remove;
    public final static int DEFAULT_ICON_APP = R.drawable.ic_launcher;
    public static final String SERVERS_LIST = "servers_list";
    public static final String PREF_SPLIT = "°SPLIT°°";

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
        /**
         *
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

//    public static class PasswordDialog extends Activity implements View.OnClickListener {
//
//        private EditText password;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState){
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.password_dialog);
//            password = (EditText) findViewById(R.id.entry_password);
//            Button ok = (Button) findViewById(R.id.ok_button);
//            ok.setOnClickListener(this);
//            Button cancel = (Button) findViewById(R.id.cancel_button);
//            cancel.setOnClickListener(this);
//            setTitle("Password");
//        }
//
//        @Override
//        public void onClick(View view) {
//            Intent intent = getIntent();
//            int id = view.getId();
//            switch (id){
//                case R.id.ok_button:
//                    String result = password.getText().toString();
//                    intent.putExtra("pass", result);
//                    setResult(RESULT_OK, intent);
//                    break;
//                case R.id.cancel_button:
//                    setResult(RESULT_CANCELED, intent);
//                    break;
//            }
//            finish();
//        }
//    }

    public static SharedPreferences getPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
