package com.vophamtuananh.base.imageloader;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class LoadInformationKeeper {

    String url;
    int placeHolderId = 0;
    int errorHolderId = android.R.drawable.stat_notify_error;
    WeakReference<LoadingImageView> imageViewWeakReference;
    public int width;
    public int height;
    Bitmap.Config config = Bitmap.Config.RGB_565;
    ScaleType scaleType = ScaleType.NOT_SCALE;
    BitmapCallback callback;

    private LoadInformationKeeper() {

    }

    LoadingImageView getLoadingImageView() {
        if (imageViewWeakReference == null)
            return null;
        return imageViewWeakReference.get();
    }

    public static class Builder {
        LoadInformationKeeper loadInformationKeeper;

        Builder() {
            loadInformationKeeper = new LoadInformationKeeper();
        }

        void url(String url) {
            loadInformationKeeper.url = url;
        }

        void placeHolderId(int placeHolderId) {
            loadInformationKeeper.placeHolderId = placeHolderId;
        }

        void errorHolderId(int errorHolderId) {
            loadInformationKeeper.errorHolderId = errorHolderId;
        }

        public void width(int width) {
            loadInformationKeeper.width = width;
        }

        public void height(int height) {
            loadInformationKeeper.height = height;
        }

        public void config(Bitmap.Config config) {
            loadInformationKeeper.config = config;
        }

        void scaleType(ScaleType scaleType) {
            loadInformationKeeper.scaleType = scaleType;
        }

        void callBack(BitmapCallback callback) {
            loadInformationKeeper.callback = callback;
        }

        void into(LoadingImageView imageView) {
            loadInformationKeeper.imageViewWeakReference = new WeakReference<>(imageView);
        }


        LoadInformationKeeper build() {
            return loadInformationKeeper;
        }
    }

    public enum ScaleType {NOT_SCALE, CROP, CENTER_INSIDE, SCALE_FULL_WIDTH, SCALE_FULL_HEIGHT}
}
