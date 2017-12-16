package vophamtuananh.com.basetest.mainactivity;

import com.vophamtuananh.base.recyclerview.ItemComparator;

import javax.inject.Inject;

/**
 * Created by vophamtuananh on 12/16/17.
 */

public class ImageComparator implements ItemComparator<String> {

    @Inject
    public ImageComparator() {

    }

    @Override
    public boolean areItemsTheSame(String item1, String item2) {
        return item1.equals(item2);
    }

    @Override
    public boolean areContentsTheSame(String item1, String item2) {
        return item1.equals(item2);
    }
}
