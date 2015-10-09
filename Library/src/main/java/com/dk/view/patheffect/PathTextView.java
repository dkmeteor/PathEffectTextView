package com.dk.view.patheffect;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DK on 2015/4/1.
 */
public class PathTextView extends View {
    private static final float BASE_SQUARE_UNIT = 72f;
    private String mText = "FUCK";
    private ArrayList<float[]> mDatas;
    private ArrayList<Path> mPaths = new ArrayList<Path>();
    private Paint mPaint = new Paint();
    private ObjectAnimator mSvgAnimator;
    private final Object mSvgLock = new Object();
    private float mPhase;
    private Type mType = Type.SINGLE;
    private float mScaleFactor = 1.0f;

    private int mTextColor = Color.BLACK;
    private float mTextSize = BASE_SQUARE_UNIT;
    private float mTextWeight = 2;
    private float mShadowDy = 0;

    private int mDuration = 3000;

    public enum Type {
        SINGLE, MULTIPLY
    }

    public PathTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(mTextWeight);

        /**
         *  refer to http://stackoverflow.com/questions/27926105/android-paint-setshadowlayer-ignoring-shadowcolor
         *
         *  The shadowLayer works only it the hardware acceleration is disabled
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        }
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mPaint.setColor(color);
    }

    public void setTextWeight(int weight) {
        mTextWeight = weight;
        mPaint.setStrokeWidth(mTextWeight);
    }

    public void setTextSize(float size) {
        mTextSize = size;
        mScaleFactor = mTextSize / BASE_SQUARE_UNIT;
    }

    public void setPaintType(Type type) {
        mType = type;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * This draws a shadow layer below the main layer, with the specified
     * offset and color, and blur radius. If radius is 0, then the shadow
     * layer is removed.
     * <p/>
     * Can be used to create a blurred shadow underneath text. Support for use
     * with other drawing operations is constrained to the software rendering
     * pipeline.
     * <p/>
     * The alpha of the shadow will be the paint's alpha if the shadow color is
     * opaque, or the alpha from the shadow color if not.
     */
    public void setShadow(int radius, int dx, int dy, int color) {
//        mPaint.clearShadowLayer();
        mShadowDy = dy;
        mPaint.setShadowLayer(radius, dx, dy, color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    public void init(String text) {
        if (text == null || text.length() == 0)
            return;

        requestLayout();
        invalidate();

        mText = text;
        mDatas = MatchPath.getPath(mText);
        mSvgAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f).setDuration(mDuration);
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
        float singlefactor = mPhase * mDatas.size();
        for (int i = 0; i < mDatas.size(); i++) {
            Path path = new Path();
            path.moveTo(mDatas.get(i)[0] * mScaleFactor + mTextWeight, mDatas.get(i)[1] * mScaleFactor + mTextWeight);
            path.lineTo(mDatas.get(i)[2] * mScaleFactor + mTextWeight, mDatas.get(i)[3] * mScaleFactor + mTextWeight);

            if (mType == Type.MULTIPLY) {
                PathMeasure measure = new PathMeasure(path, false);
                Path dst = new Path();
                measure.getSegment(0.0f, mPhase * measure.getLength(), dst, true);
                mPaths.add(dst);
            } else {
                //Fuck! can't compare float and int
                //Sometimes, at the end of animation , the value is  -9.5176697E-4 or other tiny value.
                if (singlefactor - (i + 1) >= -0.01)
                    mPaths.add(path);
                else if (i - Math.floor(singlefactor) < 0.0001) {
                    Path dst = new Path();
                    PathMeasure measure = new PathMeasure(path, false);
                    measure.getSegment(0.0f, (singlefactor % 1) * measure.getLength(), dst, true);
                    mPaths.add(dst);
                }
            }

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

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) (mText.length() * BASE_SQUARE_UNIT * mScaleFactor + getPaddingLeft()
                    + getPaddingRight() + mTextWeight * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Text wight(stoke width) may cause it a litter bigger
            result = (int) (BASE_SQUARE_UNIT * mScaleFactor) + getPaddingTop()
                    + getPaddingBottom() + (int) (mTextWeight * 2) + (int) mShadowDy;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
