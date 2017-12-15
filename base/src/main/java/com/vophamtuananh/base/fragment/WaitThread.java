package com.vophamtuananh.base.fragment;

import java.lang.ref.WeakReference;

/**
 * Created by vophamtuananh on 12/3/17.
 */

public class WaitThread extends Thread {

    private WeakReference<BaseFragment> fragmentWeak;
    private boolean isStopped;
    private final Object mObject = new Object();

    WaitThread(BaseFragment fragment) {
        fragmentWeak = new WeakReference<>(fragment);
    }

    @Override
    public void run() {
        BaseFragment fragment = fragmentWeak.get();
        if (fragment != null) {
            while (!fragment.isViewCreated() && !isStopped) {
                try {
                    synchronized (mObject) {
                        mObject.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (isStopped)
                return;

            final BaseFragment finalFragment = fragmentWeak.get();
            if (finalFragment != null) {
                finalFragment.getActivity().runOnUiThread(() -> {
                    if (finalFragment.isInitialized())
                        finalFragment.initialized();
                    else
                        finalFragment.handleAfterVisible();
                });
            }
        }
    }

    void continueProcessing() {
        synchronized (mObject) {
            mObject.notifyAll();
        }
    }

    void stopProcessing() {
        isStopped = true;
        synchronized (mObject) {
            mObject.notifyAll();
        }
    }
}
