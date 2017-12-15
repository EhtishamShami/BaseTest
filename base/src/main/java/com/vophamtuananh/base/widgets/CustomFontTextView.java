package com.vophamtuananh.base.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.vophamtuananh.base.R;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class CustomFontTextView extends AppCompatTextView {

    private static final String DEFAULT_TYPE_FONT = "fonts/AbhayaLibre-Medium.ttf";

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, defStyleAttr, 0);

        String typeFont = a.getString(R.styleable.CustomFontTextView_cftv_font);
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
    }
}
