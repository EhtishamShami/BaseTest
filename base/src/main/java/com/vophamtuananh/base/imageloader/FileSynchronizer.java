package com.vophamtuananh.base.imageloader;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vophamtuananh on 12/14/17.
 */

final class FileSynchronizer {

    private static Map<String, Object> mProcessingFiles = new ConcurrentHashMap<>();

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
                mProcessingFiles.remove(fileName);
            }
        }
    }

    boolean isProcessing(String fileName) {
        synchronized (this) {
            return mProcessingFiles.containsKey(fileName);
        }
    }
}
