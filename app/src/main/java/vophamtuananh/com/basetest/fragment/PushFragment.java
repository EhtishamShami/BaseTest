package vophamtuananh.com.basetest.fragment;

import com.vophamtuananh.base.fragment.BaseFragment;

import vophamtuananh.com.basetest.R;
import vophamtuananh.com.basetest.databinding.FragmentPushBinding;

/**
 * Created by vophamtuananh on 12/18/17.
 */

public class PushFragment extends BaseFragment<FragmentPushBinding, PushFragmentViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_push;
    }
}
