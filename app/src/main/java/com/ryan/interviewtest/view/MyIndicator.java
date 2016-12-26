package com.ryan.interviewtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.ryan.interviewtest.R;

/**
 * Created by Ryan on 2016/12/6.
 */
public class MyIndicator extends View implements ViewPager.OnPageChangeListener{
    private int mCount;
    private int mRadius;
    private float mGap;
    private int mCheckedPosition;
    private Paint mSelPaint;
    private Paint mNorPaint;
    private int mNorColor;
    private int mSelColor;
    private ViewPager mViewPager;
    private int mCurrentIndex = mCount;
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;


    public MyIndicator(Context context) {
        this(context,null);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyIndicator, defStyleAttr, 0);
        mCount = typedArray.getInteger(R.styleable.MyIndicator_count,0);
        mGap = typedArray.getDimension(R.styleable.MyIndicator_pointGap,0);
        mNorColor = typedArray.getColor(R.styleable.MyIndicator_normalPointColor,Color.argb(123,255,255,255));
        mSelColor = typedArray.getColor(R.styleable.MyIndicator_checkedPointColor,Color.WHITE);

        typedArray.recycle();
        mRadius = 10;
        initPaint();
    }

    private void initPaint(){
        mSelPaint = new Paint();
        mSelPaint.setColor(mSelColor);
        mSelPaint.setStyle(Paint.Style.FILL);
        mSelPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mNorPaint = new Paint(mSelPaint);
        mNorPaint.setColor(mNorColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY){
            mRadius = height/2;
        }
        if (widthMode != MeasureSpec.EXACTLY){
            width = (int) (2*mRadius*mCount+((mCount-1)<0? 0:(mCount-1))*mGap);
        }
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);

        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        setMeasuredDimension(width,height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (mCount >= 2) {
            for (int i = 0; i < mCount; i++) {
                if (i == mCheckedPosition) {
                    canvas.drawCircle(mRadius + i * (2 * mRadius + mGap), mRadius, mRadius, mSelPaint);
                } else {
                    canvas.drawCircle(mRadius + i * (2 * mRadius + mGap), mRadius, mRadius, mNorPaint);
                }
            }
        }
    }

    public void initIndicator(ViewPager viewPager) {
        if (viewPager == null) {
            throw new NullPointerException("viewPager is null...");
        }
        this.mViewPager = viewPager;
        if (viewPager.getAdapter() instanceof IndicatorListener) {
            mCount = ((IndicatorListener) viewPager.getAdapter()).getCorrectCount();
            mViewPager.setCurrentItem(mCount,false);
        } else {
            mCount = viewPager.getAdapter().getCount();
        }
        invalidate();
        this.mViewPager.addOnPageChangeListener(this);

    }

    public void setSelection(int position){
        mCheckedPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        setSelection(position%mCount);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE ){
            if (mCurrentIndex == 0){
                mViewPager.setCurrentItem(mCount,false);
            }else if (mCurrentIndex == mViewPager.getAdapter().getCount()-1){
                mViewPager.setCurrentItem(mCount+mCount-1,false);
            }
        }
    }


    public interface IndicatorListener {
        int getCorrectCount();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
