package com.vophamtuananh.base.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by vophamtuananh on 12/12/17.
 */

public class DiffUtilCallback<T> extends DiffUtil.Callback {

    @NonNull
    private final List<T> oldItems;

    @NonNull
    private final List<T> newItems;

    @NonNull
    private final ItemComparator<T> comparator;

    DiffUtilCallback(@NonNull final List<T> oldItems,
                     @NonNull final List<T> newItems,
                     @NonNull final ItemComparator<T> comparator) {
        this.oldItems = oldItems;
        this.newItems = newItems;
        this.comparator = comparator;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return comparator.areItemsTheSame(oldItems.get(oldItemPosition),
                newItems.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return comparator.areContentsTheSame(oldItems.get(oldItemPosition),
                newItems.get(newItemPosition));
    }
}
