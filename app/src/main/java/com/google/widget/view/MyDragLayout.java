package com.google.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/28 18:16
 * 描 述 ：侧滑面板
 * 修订历史 ：
 * ============================================================
 **/
public class MyDragLayout extends FrameLayout {

    private ViewDragHelper mHelper;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private ViewGroup mLeftView;
    private ViewGroup mMainView;
    private Status mStatus = Status.Close;

    //状态枚举
    public enum Status {
        Close, Open, Dragging;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public Status getStatus() {
        return mStatus;
    }

    //状态监听接口
    private OnDragStatusChangeListener mListener;

    public interface OnDragStatusChangeListener {
        void onClose();

        void onOpen();

        void onDragging(float fraction);
    }

    public void setOnDragStatusChangeListener(OnDragStatusChangeListener listener) {
        mListener = listener;
    }

    public MyDragLayout(Context context) {
        this(context, null);
    }

    public MyDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() < 2){
            throw new IllegalStateException("布局至少有俩孩子. Your ViewGroup must have 2 children at least.");
        }
        if(!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("子View必须是ViewGroup的子类. Your children must be an instance of ViewGroup");
        }
        mLeftView = (ViewGroup) getChildAt(0);
        mMainView = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRange = (int) (mWidth * 0.6);
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mLeftView || child == mMainView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainView) {
                left = fixedLeft(left);
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            int newleft = left;
            if (changedView == mLeftView) {
                newleft = mMainView.getLeft() + dx;
            }
            newleft = fixedLeft(newleft);
            if (changedView == mLeftView) {
                mLeftView.layout(0, 0, mWidth, mHeight);
                mMainView.layout(newleft, 0, newleft + mWidth, mHeight);
            }
            dispatchDragEvent(newleft);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (mMainView.getLeft() > mRange / 2 && xvel == 0) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }
        }
    };

    public void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        if (isSmooth) {
            if (mHelper.smoothSlideViewTo(mMainView, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainView.layout(0, 0, mWidth, mHeight);
        }
    }

    public void open() {
        open(true);
    }

    public void open(boolean isSmooth) {
        if (isSmooth) {
            if (mHelper.smoothSlideViewTo(mMainView, mRange, 0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainView.layout(mRange, 0, mRange + mWidth, mHeight);
        }
    }

    public void dispatchDragEvent(int newleft) {
        float fraction = newleft * 1.0f / mRange;
        if (mListener != null) {
            mListener.onDragging(fraction);
        }
        Status preStatus = mStatus;
        mStatus = updateDragStatus(fraction);
        if (mStatus != preStatus) {
            if (mStatus == Status.Close) {
                if (mListener != null) {
                    mListener.onClose();
                }
            } else if (mStatus == Status.Open) {
                if (mListener != null) {
                    mListener.onOpen();
                }
            }
        }
        animateViews(fraction);
    }

    private void animateViews(float fraction) {

        mLeftView.setScaleX(evaluate(fraction, 0.5f, 1.0f));
        mLeftView.setScaleY(evaluate(fraction, 0.5f, 1.0f));
        mLeftView.setTranslationX(evaluate(fraction, -mWidth/2f, 0));
        mLeftView.setAlpha(evaluate(fraction, 0.5f, 1.0f));

        mMainView.setScaleX(evaluate(fraction, 1.0f, 0.8f));
        mMainView.setScaleY(evaluate(fraction, 1.0f, 0.8f));

        getBackground().setColorFilter((Integer) evaluateColor(fraction, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    private Status updateDragStatus(float fraction) {
        if (fraction == 1.0f) {
            return Status.Open;
        } else if (fraction == 0f) {
            return Status.Close;
        }
        return Status.Dragging;
    }

    private int fixedLeft(int newleft) {
        if (newleft > mRange) {
            return mRange;
        } else if (newleft < 0) {
            return 0;
        }
        return newleft;
    }

    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
