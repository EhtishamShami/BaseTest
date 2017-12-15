package com.vophamtuananh.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

import com.vophamtuananh.base.utils.BitmapUtil;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class LoadImageRunnable implements Runnable {

    private static FileCacher mFileCacher;

    private static final Object object = new Object();

    private WeakReference<LoadCallback> mLoadCallbackWeakReference;

    private ImageHolder mImageHolder;

    LoadImageRunnable(Context context, ImageHolder imageHolder, LoadCallback loadCallback) {
        if (mFileCacher == null) {
            synchronized (object) {
                if (mFileCacher == null) {
                    mFileCacher = new FileCacher(context);
                }
            }
        }
        mLoadCallbackWeakReference = new WeakReference<>(loadCallback);
        mImageHolder = imageHolder;

    }

    @Override
    public void run() {
        Bitmap bitmap = getBitmap(mImageHolder);

        if (Thread.currentThread().isInterrupted())
            return;

        LoadCallback loadCallback = mLoadCallbackWeakReference.get();

        if (loadCallback == null)
            return;

        loadCallback.completed(mImageHolder, bitmap);
    }

    private Bitmap getBitmap(ImageHolder imageHolder) {
        File file = mFileCacher.getFile(imageHolder.url);

        Bitmap bitmap = null;
        if (file != null && file.exists())
            bitmap = decodeFile(file, imageHolder);

        if (bitmap != null)
            return bitmap;

        return fetchBitmap(file, imageHolder);
    }

    private Bitmap fetchBitmap(File file, ImageHolder imageHolder) {
        try {
            URL imageUrl = new URL(imageHolder.url);

            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);

            long size = conn.getContentLength();
            InputStream is = conn.getInputStream();
            mFileCacher.saveFile(is, file, size);
            conn.disconnect();
            return decodeFile(file, imageHolder);
        } catch (Throwable ex) {
            file.deleteOnExit();
            return null;
        }
    }

    private Bitmap decodeFile(File file, ImageHolder imageHolder) {
        Bitmap bitmap = BitmapUtil.getBitmapFromFile(file, imageHolder.config);
        switch (imageHolder.scaleType) {
            case CROP:
                return crop(bitmap, imageHolder.width, imageHolder.height);
            case CENTER_INSIDE:
                return centerIndide(bitmap, imageHolder.width, imageHolder.height);
            case SCALE_FULL_WIDTH:
                return scaleFullWidth(bitmap, imageHolder.width);
            case SCALE_FULL_HEIGHT:
                return scaleFullHeight(bitmap, imageHolder.height);
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
