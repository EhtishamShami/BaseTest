package com.vophamtuananh.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.vophamtuananh.base.utils.BitmapUtil;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class LoadImageRunnable implements Runnable {

    private static final Object object = new Object();

    private static FileCacher mFileCacher;

    private LoadCallback mLoadCallback;

    private LoadInformationKeeper mLoadInformationKeeper;

    LoadImageRunnable(Context context, LoadInformationKeeper loadInformationKeeper, LoadCallback loadCallback) {
        if (mFileCacher == null) {
            synchronized (object) {
                if (mFileCacher == null) {
                    mFileCacher = new FileCacher(context);
                }
            }
        }
        mLoadCallback = loadCallback;
        mLoadInformationKeeper = loadInformationKeeper;

    }

    @Override
    public void run() {
        Bitmap bitmap = getBitmap(mLoadInformationKeeper);

        mLoadCallback.completed(mLoadInformationKeeper, bitmap);
    }

    private Bitmap getBitmap(LoadInformationKeeper loadInformationKeeper) {
        File file = mFileCacher.getFile(loadInformationKeeper.url);
        if (file == null)
            return null;
        try {
            if (file.exists()) {
                URL imageUrl = new URL(loadInformationKeeper.url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setInstanceFollowRedirects(true);
                long size = conn.getContentLength();
                conn.disconnect();
                if (file.length() < size) {
                    file.delete();
                }
            }
        } catch (Throwable ex) {}

        Bitmap bitmap = null;
        if (file.exists())
            bitmap = decodeFile(file, loadInformationKeeper);

        if (bitmap != null)
            return bitmap;

        return fetchBitmap(file, loadInformationKeeper);
    }

    private Bitmap fetchBitmap(File file, LoadInformationKeeper loadInformationKeeper) {
        try {
            URL imageUrl = new URL(loadInformationKeeper.url);

            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);

            long size = conn.getContentLength();
            InputStream is = conn.getInputStream();
            mFileCacher.saveFile(is, file, size);
            conn.disconnect();
            if (file.exists())
                return decodeFile(file, loadInformationKeeper);
        } catch (Exception ex) {
            if (file.exists())
                file.delete();
        }
        return null;
    }

    private Bitmap decodeFile(File file, LoadInformationKeeper loadInformationKeeper) {
        Bitmap bitmap = BitmapUtil.getBitmapFromCachedFile(file, loadInformationKeeper.config);
        switch (loadInformationKeeper.scaleType) {
            case CROP:
                return crop(bitmap, loadInformationKeeper.width, loadInformationKeeper.height);
            case CENTER_INSIDE:
                return centerIndide(bitmap, loadInformationKeeper.width, loadInformationKeeper.height);
            case SCALE_FULL_WIDTH:
                return scaleFullWidth(bitmap, loadInformationKeeper.width);
            case SCALE_FULL_HEIGHT:
                return scaleFullHeight(bitmap, loadInformationKeeper.height);
        }

        return bitmap;
    }

    private Bitmap crop(Bitmap bitmap, int showWidth, int showHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleWidth = showWidth / (float) bitmapWidth;
        float scaleHeight = showHeight / (float) bitmapHeight;

        float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;

        int destWidth = (int) (bitmapWidth * scale);
        int destHeight = (int) (bitmapHeight * scale);
        bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        if (scaleWidth < scaleHeight) {
            bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - showWidth / 2, 0, showWidth, destHeight);
        } else if (scaleWidth >= scaleHeight) {
            if (scaleWidth > scaleHeight)
                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - showHeight / 2, destWidth, showHeight);
            else if (scaleWidth == scaleHeight)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, destWidth, destHeight);
        }
        return bitmap;
    }

    private Bitmap centerIndide(Bitmap bitmap, int showWidth, int showHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleWidth = showWidth / (float) bitmapWidth;
        float scaleHeight = showHeight / (float) bitmapHeight;

        float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        int destWidth = (int) (bitmapWidth * scale);
        int destHeight = (int) (bitmapHeight * scale);
        bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        return bitmap;
    }

    private Bitmap scaleFullWidth(Bitmap bitmap, int showWidth) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scale = showWidth / (float) bitmapWidth;

        if (scale == 1f) {
            return bitmap;
        } else {
            int destWidth = (int) (bitmapWidth * scale);
            int destHeight = (int) (bitmapHeight * scale);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        return bitmap;
    }

    private Bitmap scaleFullHeight(Bitmap bitmap, int showHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scale = showHeight / (float) bitmapHeight;

        if (scale == 1f) {
            return bitmap;
        } else {
            int destWidth = (int) (bitmapWidth * scale);
            int destHeight = (int) (bitmapHeight * scale);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        return bitmap;
    }
}
