package com.vophamtuananh.base.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vophamtuananh on 12/12/17.
 */

public abstract class RecyclerAdapter<VH extends RecyclerAdapter.BHolder, T> extends RecyclerView.Adapter<VH> {

    private final List<T> itemList = new ArrayList<>();

    private final ItemComparator<T> comparator;

    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter() {
        this(null, null);
    }

    public RecyclerAdapter(OnItemClickListener onItemClickListener) {
        this(null, onItemClickListener);
    }

    public RecyclerAdapter(final ItemComparator<T> comparator) {
        this(comparator, null);
    }

    public RecyclerAdapter(final ItemComparator<T> comparator, OnItemClickListener onItemClickListener) {
        this.comparator = comparator;
        this.onItemClickListener = onItemClickListener;
    }

    protected abstract VH getViewHolder(ViewGroup parent, int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindData(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(@NonNull final List<T> items) {
        if (itemList.isEmpty()) {
            updateAllItems(items);
        } else {
            updateDiffItemsOnly(items);
        }
    }

    public void appenItems(List<T> items) {
        if (itemList.isEmpty()) {
            updateAllItems(items);
        } else {
            if (items != null && !items.isEmpty()) {
                int positionStart = itemList.size() - 1;
                itemList.addAll(items);
                notifyItemRangeInserted(positionStart, items.size());
            }
        }
    }

    private void updateAllItems(@NonNull final List<T> items) {
        updateItemsInModel(items);
        notifyDataSetChanged();
    }

    private void updateDiffItemsOnly(@NonNull final List<T> items) {
        Single.fromCallable(() -> calculateDiff(items))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(__ -> updateItemsInModel(items))
                .subscribe(this::updateAdapterWithDiffResult);
    }

    private DiffUtil.DiffResult calculateDiff(@NonNull final List<T> newItems) {
        return DiffUtil.calculateDiff(new DiffUtilCallback<>(itemList, newItems, comparator));
    }

    private void updateItemsInModel(@NonNull final List<T> items) {
        itemList.clear();
        itemList.addAll(items);
    }

    private void updateAdapterWithDiffResult(@NonNull final DiffUtil.DiffResult result) {
        result.dispatchUpdatesTo(this);
    }

    public void release() {
        onItemClickListener = null;
    }

    public static class BHolder<V extends ViewDataBinding, T> extends RecyclerView.ViewHolder {

        protected V boundView;

        public BHolder(V boundView) {
            super(boundView.getRoot());
            this.boundView = boundView;
        }

        protected void bindData(T model) {
            if (model != null) {
                Method[] bindingMethods = boundView.getClass().getDeclaredMethods();
                if (bindingMethods != null && bindingMethods.length > 0) {
                    for (Method method : bindingMethods) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes != null && parameterTypes.length == 1) {
                            Class<?> clazz = parameterTypes[0];
                            try {
                                if (clazz.isInstance(model)) {
                                    method.setAccessible(true);
                                    method.invoke(boundView, model);
                                } else if (clazz.isAssignableFrom(this.getClass())) {
                                    method.setAccessible(true);
                                    method.invoke(boundView, this);
                                }
                            } catch (InvocationTargetException | IllegalAccessException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
