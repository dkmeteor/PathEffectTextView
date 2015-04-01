package com.dk.view.patheffect;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DK on 2015/4/1.
 */
public class PathTextView extends View {
    private String mText = "Hello";
    private ArrayList<float[]> mPaths;
    private Paint mPaint = new Paint();
    private ObjectAnimator mSvgAnimator;
    private final Object mSvgLock = new Object();

    private float mPhase;

    public PathTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaths = MatchPath.getPath(mText);
//        mSvgAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f).setDuration(2000);
//        mSvgAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mSvgLock) {
            Path path = new Path();
            path.moveTo(mPaths.get(0)[0], mPaths.get(0)[1]);
            path.lineTo(mPaths.get(0)[2], mPaths.get(0)[3]);
            path.moveTo(mPaths.get(1)[0], mPaths.get(1)[1]);
            path.lineTo(mPaths.get(1)[2], mPaths.get(1)[3]);
            path.moveTo(mPaths.get(2)[0], mPaths.get(2)[1]);
            path.lineTo(mPaths.get(2)[2], mPaths.get(2)[3]);

            PathMeasure measure = new PathMeasure(path, false);
            Path dst = new Path();
            dst.rewind();
            float l = measure.getLength();

            mPaint.setPathEffect(createConcaveArrowPathEffect(l, 0.7f, 32));
            boolean b = measure.getSegment(0, 0.7f * measure.getLength(), dst, true);
            canvas.drawPath(dst, mPaint);
        }
    }

    private PathEffect createConcaveArrowPathEffect(float pathLength, float phase, float offset) {
        return new PathDashPathEffect(makeConcaveArrow(5, 5), 5 * 1.2f,
                Math.max(0.5f * pathLength, offset), PathDashPathEffect.Style.ROTATE);
    }

    private void updatePathsPhaseLocked() {

    }

    private static Path makeConcaveArrow(float length, float height) {
        Path p = new Path();
        p.moveTo(-2.0f, -height / 2.0f);
        p.lineTo(length - height / 4.0f, -height / 2.0f);
        p.lineTo(length, 0.0f);
        p.lineTo(length - height / 4.0f, height / 2.0f);
        p.lineTo(-2.0f, height / 2.0f);
        p.lineTo(-2.0f + height / 4.0f, 0.0f);
        p.close();
        return p;
    }


    public float getPhase() {
        return mPhase;
    }

    public void setPhase(float phase) {
        mPhase = phase;
        synchronized (mSvgLock) {
            updatePathsPhaseLocked();
        }
        invalidate();
    }
}
