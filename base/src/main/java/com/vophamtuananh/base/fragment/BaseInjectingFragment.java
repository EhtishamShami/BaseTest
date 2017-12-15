package com.vophamtuananh.base.fragment;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vophamtuananh.base.viewmodel.FragmentViewModel;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public abstract class BaseInjectingFragment<B extends ViewDataBinding, VM extends FragmentViewModel<BaseFragment>, Component>
        extends BaseFragment<B, VM> {

    @Nullable
    private Component component;

    @Override
    public void onAttach(final Context context) {
        component = createComponent();
        if (component != null)
            onInject(component);

        super.onAttach(context);
    }

    public Component getComponent() {
        return component;
    }

    protected abstract void onInject(@NonNull final Component component);

    protected abstract Component createComponent();

}
