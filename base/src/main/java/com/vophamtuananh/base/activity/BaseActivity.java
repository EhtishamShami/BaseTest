package com.vophamtuananh.base.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vophamtuananh.base.dialog.ConfirmDialog;
import com.vophamtuananh.base.dialog.InformDialog;
import com.vophamtuananh.base.dialog.LoadingDialog;
import com.vophamtuananh.base.utils.DeviceUtil;
import com.vophamtuananh.base.utils.FileUtil;
import com.vophamtuananh.base.viewmodel.ActivityViewModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.vophamtuananh.base.utils.DeviceUtil.CAMERA_REQUEST_CODE;
import static com.vophamtuananh.base.utils.DeviceUtil.GALLERY_REQUEST_CODE;
import static com.vophamtuananh.base.utils.DeviceUtil.PERMISSION_CALL_PHONE_REQUEST_CODE;
import static com.vophamtuananh.base.utils.DeviceUtil.PERMISSION_CAMERA_REQUEST_CODE;
import static com.vophamtuananh.base.utils.DeviceUtil.PERMISSION_READ_EXTERNAL_REQUEST_CODE;
import static com.vophamtuananh.base.utils.DeviceUtil.PERMISSION_WRITE_STORAGE_REQUEST_CODE;

/**
 * Created by vophamtuananh on 12/2/17.
 */

public abstract class BaseActivity<B extends ViewDataBinding, VM extends ActivityViewModel<LifecycleOwner>> extends AppCompatActivity implements LifecycleOwner {

    protected B mViewDataBinding;

    protected VM mViewModel;

    private String mCapturedPath;

    private String mCurrentPhoneNumber;

    private LoadingDialog mLoadingDialog;

    private InformDialog mInformDialog;

    private ConfirmDialog mConfirmDialog;

    @LayoutRes
    protected abstract int getLayoutId();

    protected Class<VM> getViewModelClass() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (getViewModelClass() != null)
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
        if (mViewModel != null) {
            Method[] bindingMethods = mViewDataBinding.getClass().getDeclaredMethods();
            if (bindingMethods != null && bindingMethods.length > 0) {
                for (Method method : bindingMethods) {
                    if (method.getReturnType().equals(Void.TYPE)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes != null && parameterTypes.length == 1) {
                            Class<?> clazz = parameterTypes[0];
                            try {
                                if (clazz.isInstance(mViewModel)) {
                                    method.setAccessible(true);
                                    method.invoke(mViewDataBinding, mViewModel);
                                    break;
                                }
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            mViewModel.onCreated(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewModel != null)
            mViewModel.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mViewModel != null)
            mViewModel.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mViewDataBinding.unbind();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v != null && v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                onRejectedCameraPermission();
            }
        } else if (requestCode == PERMISSION_READ_EXTERNAL_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                onRejectedReadExternalPermission();
            }
        } else if (requestCode == PERMISSION_CALL_PHONE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callToPhoneNumber(mCurrentPhoneNumber);
            } else {
                onRejectedPhoneCallPermission();
            }
        } else if (requestCode == PERMISSION_WRITE_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onAgreedWriteExternal();
            } else {
                onRejectedWriteExternalPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (mCapturedPath != null)
                    onCapturedImage(mCapturedPath);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    onChoseImage(selectedImage);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            onChoseNoImage();
        }
    }

    public void openCamera() {
        File tempFile = FileUtil.getOutputMediaFile(getApplicationContext());
        if (tempFile != null)
            mCapturedPath = tempFile.getAbsolutePath();
        DeviceUtil.openCamera(this, tempFile);
    }

    public void openGallery() {
        DeviceUtil.openGallery(this);
    }

    public void callToPhoneNumber (String phoneNumber) {
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telMgr == null)
            return;
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return;
        }
        mCurrentPhoneNumber = phoneNumber;
        DeviceUtil.callToPhoneNumber(this, mCurrentPhoneNumber);
    }

    public void onCapturedImage(String path) {}

    public void onChoseImage(Uri uri) {}

    public void onChoseNoImage() {}

    protected void onRejectedCameraPermission() {}

    protected void onRejectedReadExternalPermission() {}

    protected void onRejectedPhoneCallPermission() {}

    protected void onAgreedWriteExternal() {}

    protected void onRejectedWriteExternalPermission() {}

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    public void showLoadingDialog(LoadingDialog.OnLoadingDilogListener onLoadingDilogListener) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (mLoadingDialog.isShowing()) {
            return;
        }

        mLoadingDialog.showWithListener(onLoadingDilogListener);
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public InformDialog getInformDialog() {
        if (mInformDialog == null)
            mInformDialog = new InformDialog(this);
        return mInformDialog;
    }

    public ConfirmDialog getConfirmDialog() {
        if (mConfirmDialog == null)
            mConfirmDialog = new ConfirmDialog(this);
        return mConfirmDialog;
    }
}
