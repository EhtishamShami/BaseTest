package com.vophamtuananh.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class FileUtil {

    private static final String PROCCESSING_DIR_NAME = "proccessing";
    private static final String CAPTURED_FILE_NAME = "captured_file";

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    private static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    public static File getOutputMediaFile(Context context) {
        File mediaStorageDir = FileUtil.getDiskCacheDir(context, PROCCESSING_DIR_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        try {
            return File.createTempFile(CAPTURED_FILE_NAME, ".jpg", mediaStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String result = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(contentUri, proj, null, null, null);
            if (cursor != null && !cursor.isClosed()) {
                int columnIndex = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(columnIndex);
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
