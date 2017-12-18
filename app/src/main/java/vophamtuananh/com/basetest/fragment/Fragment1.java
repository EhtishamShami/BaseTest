package vophamtuananh.com.basetest.fragment;

import android.arch.lifecycle.LifecycleOwner;

import com.vophamtuananh.base.fragment.BaseFragment;

import vophamtuananh.com.basetest.R;
import vophamtuananh.com.basetest.databinding.Fragment1tBinding;

/**
 * Created by vophamtuananh on 12/18/17.
 */

public class Fragment1 extends BaseFragment<Fragment1tBinding, Fragment1ViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_1t;
    }
}
