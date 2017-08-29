package com.mashjulal.android.cookmeal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.util.Locale;


public class Utils {

    public static String formatTime(long timeInMillis) {
        int[] time = getTime(timeInMillis);
        int seconds = time[2];
        int minutes = time[1];
        int hours = time[0];
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static int[] getTime(long timeInMillis) {
        long time = timeInMillis / 1000;
        int seconds = (int) time % 60;
        int minutes = (int) (time / 60) % 60;
        int hours = (int) time / 60 / 60;

        return new int[]{hours, minutes, seconds};
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
