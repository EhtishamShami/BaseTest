package com.vophamtuananh.base.imageloader;

import android.graphics.drawable.Drawable;

/**
 * Created by vophamtuananh on 12/13/17.
 */

public interface LoadingImageView {

    void setImageResource(int resourceId);

    void setImageDrawable(Drawable drawable);

    void showLoading();

    void hideLoading();
}
