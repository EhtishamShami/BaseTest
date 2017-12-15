package com.vophamtuananh.base.imageloader;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class ImageHolder {

    String url;
    int placeHolderId = 0;
    int errorHolderId = android.R.drawable.stat_notify_error;
    WeakReference<LoadingImageView> imageViewWeakReference;
    public int width;
    public int height;
    Bitmap.Config config = Bitmap.Config.RGB_565;
    ScaleType scaleType = ScaleType.NOT_SCALE;
    BitmapCallback callback;

    private ImageHolder() {

    }

    LoadingImageView getLoadingImageView() {
        if (imageViewWeakReference == null)
            return null;
        return imageViewWeakReference.get();
    }

    public static class Builder {
        ImageHolder imageHolder;

        Builder() {
            imageHolder = new ImageHolder();
        }

        void url(String url) {
            imageHolder.url = url;
        }

        void placeHolderId(int placeHolderId) {
            imageHolder.placeHolderId = placeHolderId;
        }

        void errorHolderId(int errorHolderId) {
            imageHolder.errorHolderId = errorHolderId;
        }

        public void width(int width) {
            imageHolder.width = width;
        }

        public void height(int height) {
            imageHolder.height = height;
        }

        public void config(Bitmap.Config config) {
            imageHolder.config = config;
        }

        void scaleType(ScaleType scaleType) {
            imageHolder.scaleType = scaleType;
        }

        void callBack(BitmapCallback callback) {
            imageHolder.callback = callback;
        }

        void into(LoadingImageView imageView) {
            imageHolder.imageViewWeakReference = new WeakReference<>(imageView);
        }


        ImageHolder build() {
            return imageHolder;
        }
    }

    public enum ScaleType {NOT_SCALE, CROP, CENTER_INSIDE, SCALE_FULL_WIDTH, SCALE_FULL_HEIGHT}
}
