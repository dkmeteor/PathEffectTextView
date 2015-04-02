package com.dk.view.patheffect;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DK on 2015/4/1.
 */
public class PathTextView extends View {
    private String mText = "FUCK";
    private ArrayList<float[]> mDatas;
    private ArrayList<Path> mPaths = new ArrayList<Path>();
    private Paint mPaint = new Paint();
    private ObjectAnimator mSvgAnimator;
    private final Object mSvgLock = new Object();
    private float mPhase;
    private TYPE mType = TYPE.MULTIPY;

    public enum TYPE {
        SINGLE, MULTIPY
    }

    public PathTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
    }

    public void init(String text) {
        if (text == null || text.length() == 0)
            return;

        mText = text;
        mDatas = MatchPath.getPath(mText);
        mSvgAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f).setDuration(2000);
        mSvgAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaths == null)
            return;
        synchronized (mSvgLock) {
            for (int i = 0; i < mPaths.size(); i++)
                canvas.drawPath(mPaths.get(i), mPaint);
        }
    }


    private void updatePathsPhaseLocked() {
        mPaths.clear();

        for (int i = 0; i < mDatas.size(); i++) {
            Path path = new Path();
            path.moveTo(mDatas.get(i)[0], mDatas.get(i)[1]);
            path.lineTo(mDatas.get(i)[2], mDatas.get(i)[3]);
            PathMeasure measure = new PathMeasure(path, false);
            Path dst = new Path();

            if (mType == TYPE.MULTIPY)
                measure.getSegment(0.0f, mPhase * measure.getLength(), dst, true);
            else
                measure.getSegment(0.0f, mPhase * measure.getLength(), dst, true);

            mPaths.add(dst);
        }
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
