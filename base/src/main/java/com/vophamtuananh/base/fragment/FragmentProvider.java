package com.vophamtuananh.base.fragment;

import android.support.v4.app.FragmentManager;

/**
 * Created by vophamtuananh on 12/5/17.
 */

public interface FragmentProvider<T extends BaseFragment> {

    T[] getFragments();

    int getFragmentLayoutId();

    FragmentManager getFragmentManager();

}
