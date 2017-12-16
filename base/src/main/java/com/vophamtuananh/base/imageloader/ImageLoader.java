package com.vophamtuananh.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.LruCache;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class ImageLoader {

    private static final int FADE_IN_TIME = 400;

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE_TIME = 1;

    private static int TRANSPARENT_COLOR;

    private static LruCache<String, BitmapDrawable> memoryCache;

    private static ThreadPoolExecutor executorService;

    private static final Map<LoadingImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<LoadingImageView, String>());

    private static Handler handler = new Handler();

    private WeakReference<Context> mContextWeakReference;
    
    private ImageHolder.Builder builder;

    public ImageLoader(Context context) {
        TRANSPARENT_COLOR = ContextCompat.getColor(context, android.R.color.transparent);

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>() {
        };
        executorService = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                workQueue);

        memoryCache = new LruCache<String, BitmapDrawable>((int) (Runtime.getRuntime().maxMemory() / 8)) {
            @Override
            protected int sizeOf(String url, BitmapDrawable drawable) {
                final int bitmapSize = getBitmapSize(drawable) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };

        mContextWeakReference = new WeakReference<>(context);
    }

    private void show(ImageHolder imageHolder) {
        LoadingImageView loadingImageView = imageHolder.getLoadingImageView();
        if (loadingImageView != null) {
            if (imageHolder.url == null) {
                loadingImageView.setImageResource(imageHolder.errorHolderId);
                return;
            }
            imageViews.put(loadingImageView, imageHolder.url);
            BitmapDrawable bitmapDrawable = memoryCache.get(imageHolder.url + "-" + imageHolder.width + "-" + imageHolder.height);
            if (bitmapDrawable != null) {
                loadingImageView.setImageDrawable(bitmapDrawable);
                loadingImageView.hideLoading();
            } else {
                queueDownloadPhoto(imageHolder);
                loadingImageView.showLoading();
            }
        }
    }

    private void queueDownloadPhoto(ImageHolder imgHolder) {
        if (mContextWeakReference == null)
            return;
        Context c = mContextWeakReference.get();
        if (c == null)
            return;
        executorService.submit(new LoadImageRunnable(c, imgHolder, new LoadCallback() {
            @Override
            public void completed(ImageHolder imageHolder, @Nullable Bitmap bitmap) {
                Context context = mContextWeakReference.get();
                if (context == null)
                    return;
                preDisplaying(context, bitmap, imageHolder);
            }}));
    }

    protected Bitmap reprocessBitmap(@NonNull Bitmap bitmap) {
        return bitmap;
    }

    private void preDisplaying(@NonNull Context context, @Nullable Bitmap bitmap, @NonNull ImageHolder imageHolder) {
        BitmapDrawable bitmapDrawable = null;
        if (bitmap != null) {
            bitmapDrawable = new BitmapDrawable(context.getResources(), reprocessBitmap(bitmap));
            try {
                memoryCache.put(imageHolder.url + "-" + imageHolder.width + "-" + imageHolder.height, bitmapDrawable);
            } catch (OutOfMemoryError e) {
                memoryCache.evictAll();
            }
        }
        if (imageViewReused(imageHolder))
            return;
        displayBitmap(bitmapDrawable, imageHolder);
    }

    private void displayBitmap(@Nullable BitmapDrawable bitmapDrawable, @NonNull ImageHolder imageHolder) {
        handler.post(() -> {
            BitmapCallback bitmapCallback = imageHolder.callback;
            if (bitmapCallback != null) {
                bitmapCallback.callback(bitmapDrawable);
            } else {
                setImageDrawable(imageHolder, bitmapDrawable);
            }
        });
    }

    private void setImageDrawable(@NonNull ImageHolder imageHolder, @Nullable Drawable drawable) {
        LoadingImageView loadingImageView = imageHolder.getLoadingImageView();
        if (loadingImageView == null)
            return;

        if (drawable != null) {
            final TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(TRANSPARENT_COLOR), drawable});
            loadingImageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            loadingImageView.setImageResource(imageHolder.errorHolderId);
        }

        loadingImageView.hideLoading();
    }

    private boolean imageViewReused(ImageHolder imageHolder) {
        LoadingImageView loadingImageView = imageHolder.getLoadingImageView();
        if (loadingImageView != null) {
            String tag = imageViews.get(loadingImageView);
            return tag == null || !tag.equals(imageHolder.url);
        }
        return true;
    }

    private int getBitmapSize(BitmapDrawable value) {
        return value.getBitmap().getAllocationByteCount();

    }

    public ImageLoader load(String url) {
        builder = new ImageHolder.Builder();
        builder.url(url);
        return this;
    }

    public ImageLoader placeHolderId(int placeHolderId) {
        builder.placeHolderId(placeHolderId);
        return this;
    }

    public ImageLoader errorHolderId(int errorHolderId) {
        builder.errorHolderId(errorHolderId);
        return this;
    }

    public ImageLoader width(int showWidth) {
        builder.width(showWidth);
        return this;
    }

    public ImageLoader height(int showHeight) {
        builder.height(showHeight);
        return this;
    }

    public ImageLoader config(Bitmap.Config config) {
        builder.config(config);
        return this;
    }

    public ImageLoader callBack(BitmapCallback callback) {
        builder.callBack(callback);
        return this;
    }

    public ImageLoader scaleType(ImageHolder.ScaleType scaleType) {
        builder.scaleType(scaleType);
        return this;
    }

    public void into(LoadingImageView loadingImageView) {
        builder.into(loadingImageView);
        show(builder.build());
    }
}
