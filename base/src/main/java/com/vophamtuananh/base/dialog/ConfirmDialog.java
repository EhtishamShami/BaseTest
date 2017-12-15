package com.vophamtuananh.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.databinding.DialogConfirmBinding;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class ConfirmDialog extends BDialog<DialogConfirmBinding> {

    private OnYesListener mOnYesListener;
    private OnNoListener mOnNoListener;
    private String mTitle;
    private String mDescription;
    private String mYesButtonText;
    private String mNoButtonText;
    private ConfirmType mConfirmType = ConfirmType.WARINING;
    private String mTag;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        viewDataBinding.setEvent(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupTexts();
        setupBackgrouns();
    }

    private void setupTexts() {
        viewDataBinding.tvDescription.setText(!TextUtils.isEmpty(mDescription) ? mDescription : "");
        viewDataBinding.btnYes.setText(!TextUtils.isEmpty(mYesButtonText) ? mYesButtonText : getContext().getString(R.string.yes));
        viewDataBinding.btnNo.setText(!TextUtils.isEmpty(mNoButtonText) ? mNoButtonText : getContext().getString(R.string.no));
        if (!TextUtils.isEmpty(mTitle)) {
            viewDataBinding.tvTitle.setText(mTitle);
            return;
        }
        String title;
        switch (mConfirmType) {
            case ERROR:
                title = getContext().getString(R.string.error);
                break;
            case SUCCESS:
                title = getContext().getString(R.string.success);
                break;
            default:
                title = getContext().getString(R.string.warning);
                break;
        }
        viewDataBinding.tvTitle.setText(title);
    }

    private void setupBackgrouns() {
        int backgroundParentId;
        int backgroundYestId;
        int backgroundNoId;
        int iconId;
        switch (mConfirmType) {
            case ERROR:
                backgroundParentId = R.drawable.bg_error;
                backgroundYestId = R.drawable.bg_success;
                backgroundNoId = R.drawable.bg_error;
                iconId = R.drawable.ic_error;
                break;
            case SUCCESS:
                backgroundParentId = R.drawable.bg_success;
                backgroundYestId = R.drawable.bg_success;
                backgroundNoId = R.drawable.bg_error;
                iconId = R.drawable.ic_success;
                break;
            default:
                backgroundParentId = R.drawable.bg_warning;
                backgroundYestId = R.drawable.bg_success;
                backgroundNoId = R.drawable.bg_warning;
                iconId = R.drawable.ic_warning;
                break;
        }
        viewDataBinding.llParent.setBackgroundResource(backgroundParentId);
        viewDataBinding.btnYes.setBackgroundResource(backgroundYestId);
        viewDataBinding.btnNo.setBackgroundResource(backgroundNoId);
        viewDataBinding.ivIcon.setImageResource(iconId);
    }

    public void showWithYesListener(OnYesListener onYesListener) {
        mOnYesListener = onYesListener;
        super.show();
    }

    public void showWithNoListener(OnNoListener onNoListener) {
        mOnNoListener = onNoListener;
        super.show();
    }

    public void showWithListener(OnYesListener onYesListener, OnNoListener onNoListener) {
        mOnYesListener = onYesListener;
        mOnNoListener = onNoListener;
        super.show();
    }

    public ConfirmDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public ConfirmDialog setDescription(String description) {
        mDescription = description;
        return this;
    }

    public ConfirmDialog setConfirmType(ConfirmType confirmType) {
        mConfirmType = confirmType;
        return this;
    }

    public ConfirmDialog setYesButtonText(String yesText) {
        mYesButtonText = yesText;
        return this;
    }

    public ConfirmDialog setNoButtonText(String noText) {
        mNoButtonText = noText;
        return this;
    }

    public ConfirmDialog setInformType(ConfirmType confirmType) {
        mConfirmType = confirmType;
        return this;
    }

    public ConfirmDialog setTag(String tag) {
        mTag = tag;
        return this;
    }

    public ConfirmDialog setCancelWhenTapOutSide(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public void onYesCliked() {
        if (mOnYesListener != null)
            mOnYesListener.onChoseYes(mTag);
        dismiss();
    }

    public void onNoCliked() {
        if (mOnNoListener != null)
            mOnNoListener.onChoseNo(mTag);
        dismiss();
    }

    @Override
    public void dismiss() {
        mOnYesListener = null;
        mOnNoListener = null;
        super.dismiss();
    }

    public interface OnYesListener {
        void onChoseYes(String tag);
    }

    public interface OnNoListener {
        void onChoseNo(String tag);
    }

    public enum ConfirmType {
        SUCCESS, ERROR, WARINING
    }
}
