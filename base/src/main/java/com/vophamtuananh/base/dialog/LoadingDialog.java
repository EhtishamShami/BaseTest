package com.vophamtuananh.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.databinding.DialogLoadingBinding;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class LoadingDialog extends BDialog<DialogLoadingBinding> {

    private OnLoadingDilogListener mOnLoadingDilogListener;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    public void showWithListener(OnLoadingDilogListener onLoadingDilogListener) {
        mOnLoadingDilogListener = onLoadingDilogListener;
        show();
    }

    @Override
    public void dismiss() {
        if (mOnLoadingDilogListener != null)
            mOnLoadingDilogListener.onDismissed();
        mOnLoadingDilogListener = null;
        super.dismiss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    public interface OnLoadingDilogListener {
        void onDismissed();
    }
}
