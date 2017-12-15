package com.vophamtuananh.base.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.viewmodel.FragmentViewModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by vophamtuananh on 12/3/17.
 */

public abstract class BaseFragment<B extends ViewDataBinding, VM extends FragmentViewModel<BaseFragment>> extends Fragment implements LifecycleOwner {

    protected B mViewDataBinding;

    protected VM mViewModel;

    private boolean mShouldSave = true;
    private boolean mIsInLeft;
    private boolean mIsOutLeft;
    protected boolean mIsCurrentScreen;
    private boolean mIsPush;

    private boolean mInitialized = true;
    private boolean mViewCreated = false;
    private boolean mViewDestroyed = false;

    private WaitThread mWaitThread;

    @LayoutRes
    protected abstract int getLayoutId();

    protected Class<VM> getViewModelClass() {return null;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getViewModelClass() != null)
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
        if (mViewModel != null) {
            mViewModel.onCreated(this);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation;
        if (mIsCurrentScreen) {
            int out = getPopDownAnimId();
            int in = getPopUpAnimId();
            int push_exit = getPushExitAnimId();
            int push_enter = getPushEnterAnimId();
            if (mIsPush)
                animation = AnimationUtils.loadAnimation(getContext(), enter ? in : push_exit);
            else
                animation = AnimationUtils.loadAnimation(getContext(), enter ? push_enter : out);
        } else {
            if (enter) {
                int left = getLeftInAnimId();
                int right = getRightInAnimId();
                if (mIsInLeft) {
                    if (left == 0) {
                        animation = new AlphaAnimation(1, 1);
                        animation.setDuration(getResources().getInteger(R.integer.animation_time_full));
                    } else
                        animation = AnimationUtils.loadAnimation(getContext(), left);
                } else {
                    if (right == 0) {
                        animation = new AlphaAnimation(1, 1);
                        animation.setDuration(getResources().getInteger(R.integer.animation_time_full));
                    } else
                        animation = AnimationUtils.loadAnimation(getContext(), right);
                }
            } else {
                int left = getLeftOutAnimId();
                int right = getRightOutAnimId();
                animation = AnimationUtils.loadAnimation(getContext(), mIsOutLeft ? left : right);
            }
        }

        if (enter) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mViewDestroyed)
                        return;
                    mWaitThread = new WaitThread(BaseFragment.this);
                    mWaitThread.start();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }
        return animation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
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
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onVisible();
        mViewCreated = true;
        mViewDestroyed = false;
        if (mWaitThread != null)
            mWaitThread.continueProcessing();
    }

    @Override
    public void onDestroyView() {
        if (mWaitThread != null)
            mWaitThread.stopProcessing();
        mViewDestroyed = true;
        mViewCreated = false;
        onInVisible();
        mViewDataBinding.unbind();
        super.onDestroyView();
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public void initialized() {
        mInitialized = false;
    }

    public boolean isViewCreated() {
        return mViewCreated;
    }

    public void setInLeft(boolean inLeft) {
        mIsInLeft = inLeft;
    }

    public void setOutLeft(boolean outLeft) {
        mIsOutLeft = outLeft;
    }

    public void setCurrentScreen(boolean currentScreen) {
        mIsCurrentScreen = currentScreen;
    }

    public void setPush(boolean push) {
        mIsPush = push;
    }

    public boolean isShouldSave() {
        return mShouldSave;
    }

    public void setShouldSave(boolean shouldSave) {
        this.mShouldSave = shouldSave;
    }

    protected void onVisible() {}

    protected void handleAfterVisible() {}

    protected void onInVisible() {}

    public void onCapturedImage(String path) {}

    public void onChoseImage(Uri uri) {}

    protected int getPushExitAnimId() {
        return mShouldSave ? R.anim.slide_out_left : R.anim.slide_fade_out_left;
    }

    protected int getPushEnterAnimId() {
        return mShouldSave ? R.anim.slide_in_left : R.anim.slide_fade_in_left;
    }

    protected int getPopDownAnimId() {
        return mShouldSave ? R.anim.slide_out_right : R.anim.slide_fade_out_right;
    }

    protected int getPopUpAnimId() {
        return mShouldSave ? R.anim.slide_in_right : R.anim.slide_fade_in_right;
    }

    protected int getLeftInAnimId() {return R.anim.slide_in_left;}

    protected int getRightInAnimId() {return R.anim.slide_in_right;}

    protected int getLeftOutAnimId() {return R.anim.slide_out_left;}

    protected int getRightOutAnimId() {return R.anim.slide_out_right;}
}
