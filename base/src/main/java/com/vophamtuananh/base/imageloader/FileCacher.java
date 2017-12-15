package com.vophamtuananh.base.imageloader;

import android.content.Context;

import com.vophamtuananh.base.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public class FileCacher {

    private static final String IMAGE_CACHE_DIR_NAME = "cached_image";

    private static final int MAX_SIZE = 209715200;

    private static final FileSynchronizer mFileSynchronizer = new FileSynchronizer();

    private static final Object object = new Object();

    private static long size = -1;

    private static File cacheDir;

    FileCacher(Context context) {
        if (cacheDir == null) {
            synchronized (object) {
                if (cacheDir == null) {
                    cacheDir = FileUtil.getDiskCacheDir(context, IMAGE_CACHE_DIR_NAME);
                    if (!cacheDir.exists())
                        cacheDir.mkdirs();
                    size = getDirSize(cacheDir);
                }
            }
        }
    }

    File getFile(String url) {
        if (url == null)
            return null;
        String filename = String.valueOf(url.hashCode());

        while (mFileSynchronizer.isProcessing(filename)) {
            mFileSynchronizer.waitUtilFileReleased(filename);
        }

        return new File(cacheDir, filename);
    }

    void saveFile(InputStream is, File file, long fileSize) {
        long newSize = fileSize + size;
        if (newSize > MAX_SIZE) {
            synchronized (object) {
                cleanDir(cacheDir, newSize - MAX_SIZE);
            }
        }

        while (mFileSynchronizer.isProcessing(file.getName())) {
            mFileSynchronizer.waitUtilFileReleased(file.getName());
        }
        mFileSynchronizer.registerProcess(file.getName());

        final int buffer_size = 1024;
        FileOutputStream os = null;

        if (!file.exists() || file.length() < fileSize) {
            if (file.exists())
                file.delete();
            try {
                os = new FileOutputStream(file);
                byte[] bytes = new byte[buffer_size];
                for (; ; ) {
                    int count = is.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;
                    os.write(bytes, 0, count);

                }
                size += fileSize;
            } catch (Exception ex) {
                file.deleteOnExit();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mFileSynchronizer.unRegisterProcess(file.getName());
    }

    private synchronized void cleanDir(File dir, long bytes) {
        long bytesDeleted = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                bytesDeleted += file.length();
                file.delete();

                if (bytesDeleted >= bytes) {
                    break;
                }
            }
        }
        size -= bytesDeleted;
    }

    private long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                }
            }
        }
        return size;
    }
}
