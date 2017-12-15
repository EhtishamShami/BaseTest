package com.vophamtuananh.base.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by vophamtuananh on 12/3/17.
 */

public class FragmentViewModel<V extends LifecycleOwner> extends ViewModel implements LifecycleObserver {

    @Nullable
    private volatile WeakReference<V> mViewWeakReference;

    private CompositeDisposable compositeDisposables;

    @Nullable
    protected V view() {
        final WeakReference<V> viewWeakReference = this.mViewWeakReference;
        if (viewWeakReference == null)
            return null;
        return viewWeakReference.get();
    }

    public void onCreated(V view) {
        mViewWeakReference = new WeakReference<>(view);
        if (compositeDisposables == null)
            compositeDisposables = new CompositeDisposable();
        view.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void resume() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) public void pause() {
        if (compositeDisposables != null)
            compositeDisposables.clear();
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void cleanup() {
        final WeakReference<V> viewWeakReference = this.mViewWeakReference;
        if (viewWeakReference != null) {
            V view = viewWeakReference.get();
            if (view != null) {
                view.getLifecycle().removeObserver(this);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposables != null)
            compositeDisposables.dispose();
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposables.add(disposable);
    }
}
