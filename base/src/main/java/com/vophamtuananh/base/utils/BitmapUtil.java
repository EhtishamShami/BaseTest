package com.vophamtuananh.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class BitmapUtil {

    public static void saveBitmapToFile(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat) {
        if (file == null)
            return;
        file.deleteOnExit();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(compressFormat, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null)
                bitmap.recycle();
        }
    }

    public static Bitmap getBitmapFromCachedFile(File file, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = bitmapConfig;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
    }

    public static Bitmap getBitmapFromFile(File file, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = bitmapConfig;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        return rotationBitmap(file.getAbsolutePath(), bitmap);
    }

    private static Bitmap rotationBitmap(String filePath, Bitmap bm) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String orientString = null;
        if (exif != null)
            orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        Bitmap rotatedBitmap;
        if (orientation != ExifInterface.ORIENTATION_NORMAL) {
            int rotationAngle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            rotatedBitmap = rotationBitmap(bm, rotationAngle);
        } else {
            rotatedBitmap = bm;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotationBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(orientation, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap decodeBitmapFromUri(Context context, @NonNull Uri uri,
                                             int reqWidth, int reqHeight, Bitmap.Config bitmapConfig) {
        Bitmap bitmap;
        String path;
        try {
            path = FileUtil.getRealPathFromURI(context, uri);

            if (path != null && !path.isEmpty()) {
                bitmap = decodeBitmapFromExitPath(path, reqWidth, reqHeight, bitmapConfig);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                bitmap = scaleBitmap(bitmap, reqWidth, reqHeight);
            }

            if (bitmap != null) {
                Bitmap rotatedBitmap = rotationBitmap(path, bitmap);
                if (rotatedBitmap != null) {
                    bitmap = rotatedBitmap;
                }
            }
        } catch (Exception ex) {
            bitmap = null;
            ex.printStackTrace();
        }

        return bitmap;
    }

    private static Bitmap decodeBitmapFromExitPath(String path, int reqWidth, int reqHeight, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = bitmapConfig;
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, opts);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
        int width_tmp = bitmap.getWidth(), height_tmp = bitmap.getHeight();
        if (width != 0 && height != 0) {

            while (true) {
                if (width_tmp < width || height_tmp < height)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, width_tmp, height_tmp, false);
    }

    private static int calculateInSampleSize(@NonNull BitmapFactory.Options option, int reqWidth, int reqHeight) {
        int height;
        int width;

        width = option.outWidth;
        height = option.outHeight;

        if (height == 0 || width == 0) {
            return 1;
        }

        int stretch_width = Math.round((float) width / (float) reqWidth);
        int stretch_height = Math.round((float) height / (float) reqHeight);

        if (stretch_width <= stretch_height)
            return stretch_height;
        else
            return stretch_width;
    }
}
