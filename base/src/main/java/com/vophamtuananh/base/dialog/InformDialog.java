package com.vophamtuananh.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.databinding.DialogInformBinding;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class InformDialog extends BDialog<DialogInformBinding> {

    private OnConfirmListener onConfirmListener;
    private String mTitle;
    private String mDescription;
    private String mButtonText;
    private InformType mInformType = InformType.ERROR;
    private String mTag;

    public InformDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null)
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        viewDataBinding.setEvent(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupTexts();
        setupBackgrouns();
    }

    private void setupTexts() {
        viewDataBinding.tvDescription.setText(mDescription != null ? mDescription : "");
        viewDataBinding.btnOk.setText(!TextUtils.isEmpty(mButtonText) ? mButtonText : getContext().getString(R.string.yes));
        if (!TextUtils.isEmpty(mTitle)) {
            viewDataBinding.tvTitle.setText(mTitle);
            return;
        }
        String title;
        switch (mInformType) {
            case SUCCESS:
                title = getContext().getString(R.string.success);
                break;
            case WARINING:
                title = getContext().getString(R.string.warning);
                break;
            default:
                title = getContext().getString(R.string.error);
                break;
        }
        viewDataBinding.tvTitle.setText(title);
    }

    private void setupBackgrouns() {
        int backgroundId;
        int iconId;
        switch (mInformType) {
            case SUCCESS:
                backgroundId = R.drawable.bg_success;
                iconId = R.drawable.ic_success;
                break;
            case WARINING:
                backgroundId = R.drawable.bg_warning;
                iconId = R.drawable.ic_warning;
                break;
            default:
                backgroundId = R.drawable.bg_error;
                iconId = R.drawable.ic_error;
                break;
        }
        viewDataBinding.llParent.setBackgroundResource(backgroundId);
        viewDataBinding.btnOk.setBackgroundResource(backgroundId);
        viewDataBinding.ivIcon.setImageResource(iconId);
    }

    public void onConfirmClicked() {
        if (onConfirmListener != null)
            onConfirmListener.onConfirmed(mTag);
        dismiss();
    }

    public InformDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public InformDialog setDescription(String description) {
        mDescription = description;
        return this;
    }

    public InformDialog setButtonText(String buttonText) {
        mButtonText = buttonText;
        return this;
    }

    public InformDialog setInformType(InformType informType) {
        mInformType = informType;
        return this;
    }

    public InformDialog setTag(String tag) {
        mTag = tag;
        return this;
    }

    public InformDialog setCancelWhenTapOutSide(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public void showWithListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        super.show();
    }

    @Override
    public void dismiss() {
        onConfirmListener = null;
        super.dismiss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_inform;
    }

    public interface OnConfirmListener {
        void onConfirmed(String tag);
    }

    public enum InformType {
        SUCCESS, ERROR, WARINING
    }
}
