package com.google.android.cameraview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Class which provide the bitmap functional
 */
public final class GBitmapHelper {

    /**
     * Method which provide the process, save and rotate {@link Bitmap} if it needed
     *
     * @param bitmapArray instance of the {@link Bitmap}
     * @return instance of the {@link Bitmap}
     */
    @Nullable
    public static Uri processingBitmap(@Nullable byte[] bitmapArray) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return processingBitmap(bitmap);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Method which provide the process, save and rotate {@link Bitmap} if it needed
     *
     * @param bitmap instance of the {@link Bitmap}
     * @return instance of the {@link Bitmap}
     */
    @Nullable
    public static Uri processingBitmap(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        @SuppressLint("DefaultLocale") final String fileName =
                String.format("receipt_%d", new Date().getTime());
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        final Uri uri = saveImage(bitmap, fileName);
        return uri;
    }

    /**
     * Method which provide the save of the image
     *
     * @param bitmap instance of the {@link Bitmap}
     * @param name   {@link String} value of the name
     */
    @Nullable
    protected static Uri saveImage(@Nullable Bitmap bitmap,
                                   @Nullable String name) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String imageName = String.format("%s.jpg", name);
        File file = new File(myDir, imageName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(file);
    }
}
