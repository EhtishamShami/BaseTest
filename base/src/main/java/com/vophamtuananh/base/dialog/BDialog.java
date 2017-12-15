package com.vophamtuananh.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Window;

import com.vophamtuananh.base.R;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public abstract class BDialog<T extends ViewDataBinding> extends Dialog {

    protected T viewDataBinding;

    public BDialog(@NonNull Context context) {
        super(context);
    }

    protected abstract int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(), null, false);
        setContentView(viewDataBinding.getRoot());
        getWindow().setWindowAnimations(R.style.DialogTheme);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setOnCancelListener(null);
    }

}
