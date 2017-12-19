package com.vophamtuananh.base.imageloader;

import android.graphics.drawable.Drawable;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public interface ILoadingImageView {

    void setResourceId(int resourceId);

    void setDrawable(Drawable drawable);

    void showLoading();

    void hideLoading();
}
