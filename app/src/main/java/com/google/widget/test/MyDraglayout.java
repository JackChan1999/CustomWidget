package com.google.widget.test;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：Widgets
 * Package_Name：com.google.widget
 * Version：1.0
 * time：2016/2/15 14:09
 * des ：
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class MyDraglayout extends FrameLayout {

    private View mLeftContent;
    private View mRightContent;
    private View mMainContent;
    private int mWidth;
    private int mHeight;
    private int mRightWidth;
    private int mRangeLeft;
    private int mRangeRight;

    private Status mStatus;
    private Direction mDirection = Direction.Left;
    private OnDragListener mListener;
    private ViewDragHelper mHelper;


    public MyDraglayout(Context context) {
        super(context);
    }

    public MyDraglayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = ViewDragHelper.create(this, mCallback);
        GestureDetectorCompat detector = new GestureDetectorCompat(getContext(), new
                GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                            float distanceY) {
                        return Math.abs(distanceX) > Math.abs(distanceY);
                    }
                });
    }

    //监听接口
    //状态枚举


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftContent = getChildAt(0);
        mRightContent = getChildAt(1);
        mMainContent = getChildAt(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMainContent.getMeasuredWidth();
        mHeight = mMainContent.getMeasuredHeight();
        mRightWidth = mRightContent.getMeasuredWidth();
        mRangeLeft = (int) (mWidth * 0.6f);
        mRangeRight = mRightWidth;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void layoutContent(){
        mLeftContent.layout(0,0,mWidth,mHeight);
        mMainContent.layout(mMainLeft,0,mMainLeft+mWidth,mHeight);
        mRightContent.layout(mWidth-mRightWidth,0,mWidth,mHeight);
    }

    private int mMainLeft = 0;
    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return clampResult(mMainLeft+dx,left);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mMainContent){
                mMainLeft = left;
            }else {
                mMainLeft += dx;
            }
            mMainLeft = clampResult(mMainLeft,mMainLeft);
            if (changedView == mMainContent || changedView == mLeftContent){
                layoutContent();
            }

            //dispatchDragEvent();
            invalidate();

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    };

    private void dispatchDragEvent(int mainLeft) {
        float fraction =0;
        if (mDirection == Direction.Left){
            fraction = mainLeft/(float)mRangeLeft;
        }else if (mDirection == Direction.Right){
            fraction = Math.abs(mainLeft)/mRangeRight;
        }
    }

    private int clampResult(int temp, int left) {
        Integer minLeft = null;
        Integer maxLeft = null;
        if (mDirection == Direction.Left){
            minLeft = 0;
            maxLeft = mRangeLeft;
        }else if (mDirection == Direction.Right){
            minLeft = -mRangeRight;
            maxLeft = 0;
        }

        if (minLeft != null && temp < minLeft){
            return minLeft;
        }else if (minLeft != null && temp > maxLeft){
            return maxLeft;
        }else {
            return left;
        }

    }

    //状态监听接口

    public interface OnDragListener {
        void onClose();

        void onOpen();

        void onDrag();

        void onStartOpen();
    }

    public void setListener(OnDragListener listener) {
        mListener = listener;
    }

    public OnDragListener getListener() {
        return mListener;
    }

    //状态枚举
    private enum Status {
        Close, Open, Dragging;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public Status getStatus() {
        return mStatus;
    }

    //方向枚举
    private enum Direction {
        Left, Right, defualt;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public Direction getDirection() {
        return mDirection;
    }
}
