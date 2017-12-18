package com.vophamtuananh.base.viewmodel;

import android.arch.lifecycle.LifecycleOwner;

/**
 * Created by vophamtuananh on 12/18/17.
 */

public interface CommonView extends LifecycleOwner {

    void showLoading();

    void showError();

    void hideLoading();
}
