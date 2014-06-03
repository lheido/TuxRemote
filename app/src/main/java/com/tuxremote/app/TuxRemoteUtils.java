package com.tuxremote.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lheido on 03/06/14.
 */
public class TuxRemoteUtils {

    public final static String SPLIT_CHAR = ":";
    public final static int DEFAULT_CLOSE_RES = R.drawable.ic_action_content_remove;
    public final static int DEFAULT_ICON_APP = R.drawable.ic_launcher;
    /**
     * Write image to internal storage
     * @param context
     * @param fileName
     * @param image    : Bitmap image format PNG only
     */
    public static void saveImageToInternalStorage(Context context, String fileName, Bitmap image){
        // Convert Bitmap to byteArray
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Open fileOutput with fileName and write byteArray
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(byteArray);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
