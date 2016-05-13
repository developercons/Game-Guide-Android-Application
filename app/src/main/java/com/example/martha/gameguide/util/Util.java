package com.example.martha.gameguide.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.listener.DecodeBitmapListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * Created by Martha on 4/8/2016.
 */
public class Util {

    public static Random random = new Random();

    public static void decodeByteArrayForSizeAsync(final byte[] imageBytes, final int reqWidth, final int reqHeight, final DecodeBitmapListener listener) {
        if (listener == null) throw new RuntimeException("DecodeBitmapListener must not be null");
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
            listener.onComplete(decodedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail();
        }
    }

    public static String getImageFilePath(Intent data, ContentResolver contentResolver) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight){
        BitmapFactory.Options options =  new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream imageStream,
                                                       int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(imageStream, null, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int wdif = options.outWidth / reqWidth;
        int hdif = options.outHeight / reqHeight;

        return Math.max(wdif, hdif);
    }


    public static void cacheObject(Context context, String key, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Failed to cache data.");
            e.printStackTrace();
        }
    }

    public static Object readCachedObject(Context context, String key){
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException e) {
            System.out.println("Failed to cache data.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isPhoneValid(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPasswordValid(String password) {
        return password != null && password.matches("[a-zA-Z0-9]+");
    }
    public static boolean isFieldValid(String field, int charQuantity){
        return field != null && !field.isEmpty();
    }

    public static void markInvalidField(TextView field, Animation animation) {
        field.setAnimation(animation);
        field.setTextColor(ContextCompat.getColor(field.getContext(), R.color.error_red));
        field.startAnimation(animation);
    }
    public static void markEmptyField(TextView field, Animation animation) {
        field.setAnimation(animation);
        field.startAnimation(animation);
    }


}