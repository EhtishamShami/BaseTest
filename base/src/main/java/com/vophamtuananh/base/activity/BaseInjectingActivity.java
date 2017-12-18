package com.vophamtuananh.base.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vophamtuananh.base.viewmodel.ActivityViewModel;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public abstract class BaseInjectingActivity<B extends ViewDataBinding, VM extends ActivityViewModel, Component>
        extends BaseActivity<B, VM> {

    @Nullable
    private Component component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        component = createComponent();
        if (component != null)
            onInject(component);

        super.onCreate(savedInstanceState);
    }

    public Component getComponent() {
       return component;
    }

    protected abstract void onInject(@NonNull final Component component);

    protected abstract Component createComponent();
}
