package com.vophamtuananh.base.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Stack;

import javax.inject.Inject;

/**
 * Created by vophamtuananh on 12/5/17.
 */

public class BaseFragmentHelper<T extends BaseFragment> {

    private ArrayList<Stack<T>> mPageList;
    private int mPageIndex;
    private int mLayoutId;
    private FragmentManager mFragmentManager;

    private T[] mBuildFragments;

    private OnChangedFragmentListener mOnChangedFragmentListener;

    @Inject
    public BaseFragmentHelper(FragmentProvider<T> fragmentProvider, int shouldShowPosition) {
        this.mLayoutId = fragmentProvider.getContentLayoutId();
        this.mFragmentManager = fragmentProvider.fragmentManager();
        this.mBuildFragments = fragmentProvider.getFragments();
        initFragments(mBuildFragments, shouldShowPosition);
    }

    private void initFragments(T[] fragments, int shouldShowPosition) {
        this.mPageList = new ArrayList<>(fragments.length);

        for (T fragment : fragments) {
            Stack<T> stack = new Stack<>();
            stack.push(fragment);
            this.mPageList.add(stack);
        }

        T fragment = mPageList.get(mPageIndex).peek();
        if (fragment.isAdded() || fragment.isDetached()) {
            this.showFragment(mPageIndex);
        } else {
            if (mOnChangedFragmentListener != null)
                mOnChangedFragmentListener.onChangedFragment(fragment);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(mLayoutId, fragment);
            transaction.commitAllowingStateLoss();
        }
        if (shouldShowPosition != mPageIndex) {
            this.showFragment(shouldShowPosition);
        }
    }

    public void pushFragment(T fragment) {
        T hideFragment = mPageList.get(mPageIndex).peek();
        mPageList.get(mPageIndex).push(fragment);
        if (mOnChangedFragmentListener != null)
            mOnChangedFragmentListener.onChangedFragment(fragment);

        fragment.setCurrentScreen(true);
        fragment.setPush(true);
        hideFragment.setCurrentScreen(true);
        hideFragment.setPush(true);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (hideFragment.isShouldSave())
            transaction.hide(hideFragment);
        else
            transaction.detach(hideFragment);

        transaction.add(mLayoutId, fragment);
        transaction.commitAllowingStateLoss();

    }

    public boolean popFragment() {
        return popFragment(1);
    }

    public boolean popFragmentToRoot() {
        int level = mPageList.get(mPageIndex).size() - 1;
        return popFragment(level);
    }

    private boolean popFragment(int level) {
        if (level <= 0) return false;
        if (mPageList.get(mPageIndex).size() <= level) return false;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        while (level >= 1) {
            T fragment = mPageList.get(mPageIndex).pop();
            fragment.setCurrentScreen(true);
            fragment.setPush(false);
            transaction.remove(fragment);
            level--;
        }
        T showFragment = mPageList.get(mPageIndex).peek();

        if (mOnChangedFragmentListener != null)
            mOnChangedFragmentListener.onChangedFragment(showFragment);

        showFragment.setCurrentScreen(true);
        showFragment.setPush(false);

        if (showFragment.isHidden())
            transaction.show(showFragment);
        else if (showFragment.isDetached())
            transaction.attach(showFragment);

        transaction.commitAllowingStateLoss();
        return true;
    }


    public void showFragment(int index) {
        if (index == mPageIndex) return;
        T showFragment = mPageList.get(index).peek();
        T hideFragment = mPageList.get(mPageIndex).peek();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mPageIndex > index) {
            showFragment.setInLeft(true);
            hideFragment.setOutLeft(false);
        } else {
            showFragment.setInLeft(false);
            hideFragment.setOutLeft(true);
        }
        showFragment.setCurrentScreen(false);
        hideFragment.setCurrentScreen(false);
        mPageIndex = index;

        if (showFragment.isDetached() || showFragment.isAdded()) {
            transaction.attach(showFragment);
        } else {
            transaction.add(mLayoutId, showFragment);
        }
        transaction.detach(hideFragment);
        transaction.commitAllowingStateLoss();
        if (mOnChangedFragmentListener != null)
            mOnChangedFragmentListener.onChangedFragment(showFragment);
    }

    public void release() {
        if (mBuildFragments != null && mBuildFragments.length > 0) {
            for (T fragment : mBuildFragments) {
                fragment = null;
            }
        }
        mBuildFragments = null;
        mOnChangedFragmentListener = null;
        mFragmentManager = null;
    }

    public interface OnChangedFragmentListener {
        void onChangedFragment(BaseFragment fragment);
    }
}
