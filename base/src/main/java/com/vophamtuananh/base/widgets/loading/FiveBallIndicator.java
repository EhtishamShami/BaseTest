package com.vophamtuananh.base.widgets.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by vophamtuananh on 12/5/17.
 */

public class FiveBallIndicator extends Indicator {

    private static final int COLOR = Color.parseColor("#DDFFFFFF");
    private static final int BALL_COUNT = 5;
    private static final int SPACING = 10;

    private static final float SCALE = 1.0f;

    private float[] scaleFloats = new float[] {SCALE, SCALE, SCALE, SCALE, SCALE, SCALE};

    private float radius = 0f;

    FiveBallIndicator() {
        super(COLOR);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (radius == 0f)
            radius = (getWidth() - ((BALL_COUNT - 1) * SPACING)) / (BALL_COUNT * 2);
        float x = radius;
        float y = getHeight() / 2;
        for (int i = 0; i < BALL_COUNT; i++) {
            canvas.save();
            canvas.translate(x, y);
            x += (radius * 2 + SPACING);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int[] delays = new int[]{80, 160, 240, 320, 400, 480};
        for (int i = 0; i < BALL_COUNT; i++) {
            final int index = i;

            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);

            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);

            addUpdateListener(scaleAnim, valueAnimator ->  {
                scaleFloats[index] = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            });
            animators.add(scaleAnim);
        }
        return animators;
    }
}
