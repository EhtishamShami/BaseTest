package com.vophamtuananh.base.imageloader;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by vophamtuananh on 12/14/17.
 */

interface LoadCallback {

    void completed(ImageHolder imageHolder, @Nullable Bitmap bitmap);

}
