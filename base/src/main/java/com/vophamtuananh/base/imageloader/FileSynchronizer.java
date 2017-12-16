package com.vophamtuananh.base.imageloader;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vophamtuananh on 12/14/17.
 */

final class FileSynchronizer {

    private static final Map<String, Object> mProcessingFiles = new ConcurrentHashMap<>();

    FileSynchronizer() {

    }

    void registerProcess(String fileName) {
        synchronized (this) {
            mProcessingFiles.put(fileName, new Object());
        }
    }

    void unRegisterProcess(String fileName) {
        synchronized (this) {
            if (mProcessingFiles.containsKey(fileName)) {
                synchronized (mProcessingFiles.get(fileName)) {
                    mProcessingFiles.get(fileName).notifyAll();
                }
                mProcessingFiles.remove(fileName);
            }
        }
    }

    boolean isProcessing(String fileName) {
        synchronized (this) {
            return mProcessingFiles.containsKey(fileName);
        }
    }

    void waitUtilFileReleased(String fileName) {
        synchronized (this) {
            if (mProcessingFiles.containsKey(fileName)) {
                try {
                    mProcessingFiles.get(fileName).wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
