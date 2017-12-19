package com.vophamtuananh.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE_TIME = 1;

    private static final FileSynchronizer mFileSynchronizer = new FileSynchronizer();

    private static List<LoadInformationKeeper> mWaitingKeepers = new ArrayList<>();

    private static LruCache<String, BitmapDrawable> memoryCache;

    private static ThreadPoolExecutor executorService;

    private static final Map<ILoadingImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ILoadingImageView, String>());

    private static Handler handler = new Handler();

    private WeakReference<Context> mContextWeakReference;
    
    private LoadInformationKeeper.Builder builder;

    public ImageLoader(Context context) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>() {};
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

    private boolean show(LoadInformationKeeper loadInformationKeeper) {
        ILoadingImageView ILoadingImageView = loadInformationKeeper.getLoadingImageView();
        if (ILoadingImageView != null) {
            if (loadInformationKeeper.url == null) {
                ILoadingImageView.setResourceId(loadInformationKeeper.errorHolderId);
                return true;
            }

            imageViews.put(ILoadingImageView, loadInformationKeeper.url);

            String fileName = String.valueOf(loadInformationKeeper.url.hashCode());
            if (mFileSynchronizer.isProcessing(fileName)) {
                if (!mWaitingKeepers.contains(loadInformationKeeper))
                    mWaitingKeepers.add(loadInformationKeeper);
                return false;
            }

            if (imageViewReused(loadInformationKeeper))
                return true;

            BitmapDrawable bitmapDrawable = memoryCache.get(loadInformationKeeper.url + "-" + loadInformationKeeper.width + "-" + loadInformationKeeper.height);
            if (bitmapDrawable != null) {
                ILoadingImageView.setDrawable(bitmapDrawable);
                ILoadingImageView.hideLoading();
            } else {
                mFileSynchronizer.registerProcess(fileName);
                queueDownloadPhoto(loadInformationKeeper);
                ILoadingImageView.showLoading();
            }
        }
        return true;
    }

    private void notifyWaitingKeepers() {
        if (mContextWeakReference == null || mContextWeakReference.get() == null) {
            mWaitingKeepers.clear();
            return;
        }

        Iterator<LoadInformationKeeper> iter = mWaitingKeepers.iterator();

        while (iter.hasNext()) {
            LoadInformationKeeper loadInformationKeeper = iter.next();
            if (show(loadInformationKeeper))
                iter.remove();
        }
    }

    private void queueDownloadPhoto(LoadInformationKeeper informationKeeper) {
        Context c = mContextWeakReference.get();
        if (c == null) {
            String fileName = String.valueOf(informationKeeper.url.hashCode());
            mFileSynchronizer.unRegisterProcess(fileName);
            notifyWaitingKeepers();
            return;
        }
        executorService.submit(new LoadImageRunnable(c, informationKeeper, (loadInformationKeeper, bitmap) -> {
            Context context = mContextWeakReference.get();
            if (context == null) {
                String fileName = String.valueOf(loadInformationKeeper.url.hashCode());
                mFileSynchronizer.unRegisterProcess(fileName);
                handler.post(this::notifyWaitingKeepers);
                return;
            }
            preDisplaying(context, bitmap, loadInformationKeeper);
        }));
    }

    protected Bitmap reprocessBitmap(@NonNull Bitmap bitmap) {
        return bitmap;
    }

    private void preDisplaying(@NonNull Context context, @Nullable Bitmap bitmap, @NonNull LoadInformationKeeper loadInformationKeeper) {
        BitmapDrawable bitmapDrawable = null;
        if (bitmap != null) {
            bitmapDrawable = new BitmapDrawable(context.getResources(), reprocessBitmap(bitmap));
            try {
                memoryCache.put(loadInformationKeeper.url + "-" + loadInformationKeeper.width + "-" + loadInformationKeeper.height, bitmapDrawable);
            } catch (OutOfMemoryError e) {
                memoryCache.evictAll();
            }
        }
        String fileName = String.valueOf(loadInformationKeeper.url.hashCode());
        mFileSynchronizer.unRegisterProcess(fileName);
        if (imageViewReused(loadInformationKeeper)) {
            handler.post(this::notifyWaitingKeepers);
            return;
        }
        displayBitmap(bitmapDrawable, loadInformationKeeper);
    }

    private void displayBitmap(@Nullable BitmapDrawable bitmapDrawable, @NonNull LoadInformationKeeper loadInformationKeeper) {
        handler.post(() -> {
            BitmapCallback bitmapCallback = loadInformationKeeper.callback;
            if (bitmapCallback != null) {
                bitmapCallback.callback(bitmapDrawable);
            } else {
                setImageDrawable(loadInformationKeeper, bitmapDrawable);
            }
            notifyWaitingKeepers();
        });
    }

    private void setImageDrawable(@NonNull LoadInformationKeeper loadInformationKeeper, @Nullable Drawable drawable) {
        ILoadingImageView ILoadingImageView = loadInformationKeeper.getLoadingImageView();
        if (ILoadingImageView == null)
            return;

        if (drawable != null) {
            ILoadingImageView.setDrawable(drawable);
        } else {
            ILoadingImageView.setResourceId(loadInformationKeeper.errorHolderId);
        }

        ILoadingImageView.hideLoading();
    }

    private boolean imageViewReused(LoadInformationKeeper loadInformationKeeper) {
        ILoadingImageView ILoadingImageView = loadInformationKeeper.getLoadingImageView();
        if (ILoadingImageView != null) {
            String tag = imageViews.get(ILoadingImageView);
            return tag == null || !tag.equals(loadInformationKeeper.url);
        }
        return true;
    }

    private int getBitmapSize(BitmapDrawable value) {
        return value.getBitmap().getAllocationByteCount();

    }

    public ImageLoader load(String url) {
        builder = new LoadInformationKeeper.Builder();
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

    public ImageLoader scaleType(LoadInformationKeeper.ScaleType scaleType) {
        builder.scaleType(scaleType);
        return this;
    }

    public void into(ILoadingImageView ILoadingImageView) {
        builder.into(ILoadingImageView);
        show(builder.build());
    }
}
