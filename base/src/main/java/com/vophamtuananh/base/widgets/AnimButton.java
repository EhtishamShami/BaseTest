package com.vophamtuananh.base.widgets;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.vophamtuananh.base.R;

/**
 * Created by vophamtuananh on 10/4/17.
 */

public class AnimButton extends AppCompatButton {

    private static final String DEFAULT_TYPE_FONT = "fonts/AbhayaLibre-Medium.ttf";

    public AnimButton(Context context) {
        super(context);
    }

    public AnimButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimButton, defStyleAttr, 0);

        String typeFont = a.getString(R.styleable.AnimButton_ab_font);
        if (TextUtils.isEmpty(typeFont))
            typeFont = DEFAULT_TYPE_FONT;

        a.recycle();

        Typeface typeface;
        try {
            typeface = Typeface.createFromAsset(getContext().getAssets(), typeFont);
        } catch (Exception e) {
            return;
        }
        setTypeface(typeface);

        init(context);
    }

    private void init(Context context) {
        StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.common_button_anim);
        setStateListAnimator(stateListAnimator);
    }
}
