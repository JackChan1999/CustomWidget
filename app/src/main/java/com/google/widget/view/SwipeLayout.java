package com.google.widget.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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
public class SwipeLayout extends FrameLayout {


    private ViewDragHelper mHelper;
    private View mFrontView;
    private View mBackView;
    private int mRange;
    private int mHeight;
    private int mWidth;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = ViewDragHelper.create(this, mCallback);
    }

    //状态枚举
    private Status mStatus = Status.Close;

    private enum Status{
        Close,Open,Dragging;
    }

    public void setStatus(Status status){
        mStatus = status;
    }

    public Status getStatus(){
        return mStatus;
    }

    //状态监听
    private OnSwipeStatusChangeListener mListener;

    public interface OnSwipeStatusChangeListener{
        void onClose(SwipeLayout swipeLayout);
        void onOpen(SwipeLayout swipeLayout);
        void onDragging(SwipeLayout swipeLayout);
        void onStartClose(SwipeLayout swipeLayout);
        void onStartOpen(SwipeLayout swipeLayout);
    }

    public void setListener(OnSwipeStatusChangeListener listener) {
        mListener = listener;
    }

    public OnSwipeStatusChangeListener getListener() {
        return mListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutContent(false);
    }

    private void layoutContent(boolean isOpen) {
        Rect frontRect = computeFrontRect(isOpen);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);

        Rect backRect = computeBackRect(frontRect);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
    }

    private Rect computeBackRect(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left,0,left+mRange,mHeight);
    }

    private Rect computeFrontRect(boolean isOpen) {
        int left = 0;
        if (isOpen) left = -mRange;
        return new Rect(left,0,left+mWidth,mHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mHelper.processTouchEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mFrontView.getMeasuredWidth();
        mHeight = mFrontView.getMeasuredHeight();
        mRange = mBackView.getMeasuredWidth();
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mFrontView || child == mBackView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mFrontView){
                if (left < -mRange){
                    return -mRange;
                }else if (left > 0){
                    return 0;
                }
            }else if (child == mBackView){
                if (left > mWidth){
                    return mWidth;
                }else if (left < mWidth - mRange){
                    return mWidth - mRange;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mFrontView){
                mBackView.offsetLeftAndRight(dx);
            }else if (changedView == mBackView){
                mFrontView.offsetLeftAndRight(dx);
            }
            dispatchSwipeEvent();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel == 0 && mFrontView.getLeft() < -mRange/2.0f){
                open();
            }else if (xvel < 0){
                open();
            }else {
                close();
            }
        }
    };

    public void close() {
        close(true);
    }

    private void close(boolean isSmooth) {
        if (isSmooth){
            if (mHelper.smoothSlideViewTo(mFrontView,0,0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            layoutContent(false);
        }
    }

    public void open() {
        open(true);
    }

    private void open(boolean isSmooth) {
        if (isSmooth){
            if (mHelper.smoothSlideViewTo(mFrontView,-mRange,0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            layoutContent(true);
        }
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void dispatchSwipeEvent() {
        if (mListener != null){
            mListener.onDragging(this);
        }
        Status preStatus = mStatus;
        mStatus = updateStatus();
        if (mStatus != preStatus && mListener != null){
            if (mStatus == Status.Close){
                    mListener.onClose(this);
            }else if (mStatus == Status.Open){
                    mListener.onOpen(this);
            }else if (mStatus == Status.Dragging){
                if (preStatus == Status.Close){
                    mListener.onStartOpen(this);
                }else if (preStatus == Status.Open){
                    mListener.onStartClose(this);
                }
            }
        }
    }

    private Status updateStatus() {
        if (mFrontView.getLeft() == -mRange){
            return Status.Open;
        }else if (mFrontView.getLeft() == 0){
            return Status.Close;
        }
        return Status.Dragging;
    }
}
