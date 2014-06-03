package com.tuxremote.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lheido on 03/06/14.
 */
public class TuxRemoteUtils {

    /**
     * Write image to internal storage
     * @param context
     * @param fileName
     * @param image    : Bitmap image format PNG only
     */
    public static void storeImage(Context context, String fileName, Bitmap image){
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

    /**
     * load an image from internal storage
     * @param context
     * @param fileName
     * @return drawable : image, use imageView.setImageDrawable(drawable);
     */
    public static Drawable loadImageFromInternalStorage(Context context, String fileName){
        File filePath = context.getFileStreamPath(fileName);
        return Drawable.createFromPath(filePath.toString());
    }
}
