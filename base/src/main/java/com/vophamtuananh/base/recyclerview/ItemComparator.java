package com.vophamtuananh.base.recyclerview;

/**
 * Created by vophamtuananh on 12/12/17.
 */

public interface ItemComparator<T> {

    boolean areItemsTheSame(final T item1, final T item2);

    boolean areContentsTheSame(final T item1, final T item2);
}
