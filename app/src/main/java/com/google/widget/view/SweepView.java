package com.google.widget.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/28 09:15
 * 描 述 ：侧滑删除控件
 * 修订历史 ：
 * ============================================================
 **/
public class SweepView extends ViewGroup {

    private static final String TAG = "SweepView";
    private ViewDragHelper mHelper;
    private View mContentView;
    private View mDeleteView;

    private int mDeleteWidth;
    private int mDeleteHeight;

    private int mContentWidth;
    private int mContentHeight;

    public SweepView(Context context) {
        this(context, null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = ViewDragHelper.create(this,mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);
        mDeleteWidth = mDeleteView.getLayoutParams().width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mContentView.measure(widthMeasureSpec,heightMeasureSpec);

        int deleteWidthSpec = MeasureSpec.makeMeasureSpec(mDeleteWidth, MeasureSpec.EXACTLY);
        mDeleteView.measure(deleteWidthSpec,heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentWidth = mContentView.getMeasuredWidth();
        mContentHeight = mContentView.getMeasuredHeight();
        mDeleteHeight = mDeleteView.getMeasuredHeight();

        mContentView.layout(l,t,r,b);
        mDeleteView.layout(getWidth(),0,getWidth()+mDeleteWidth,b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDeleteView || child == mContentView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mDeleteView){
                if (left < mContentWidth - mDeleteWidth){
                    return mContentWidth - mDeleteWidth;
                }else if (left > mContentWidth - mDeleteWidth){
                    return mContentWidth - mDeleteWidth;
                }
            }else if (child == mContentView){
                if (left < 0 && left < -mDeleteWidth){
                    return -mDeleteWidth;
                }else if (left > 0){
                    return 0;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            if (changedView == mContentView){
                mDeleteView.layout(mContentWidth+left,0,mContentWidth+left+mDeleteWidth,mDeleteHeight);
                //mDeleteView.offsetLeftAndRight(dx);
            }else if (changedView == mDeleteView){
                mContentView.layout(left - mContentWidth, 0, left, mContentHeight);
                //mContentView.offsetLeftAndRight(dx);
            }

            ViewCompat.postInvalidateOnAnimation(SweepView.this);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left = mContentView.getLeft();
            if (left < -mDeleteWidth/2){
                open();
            }else {
                close();
            }
        }
    };

    private boolean isOpen;

    public void close() {
        isOpen = false;
        if (mListener != null){
            mListener.onSweepChange(this,isOpen);
        }
        mHelper.smoothSlideViewTo(mDeleteView,mContentWidth,0);
        mHelper.smoothSlideViewTo(mContentView,0,0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void open() {
        isOpen = true;
        if (mListener != null){
            mListener.onSweepChange(this,isOpen);
        }
        mHelper.smoothSlideViewTo(mDeleteView,mContentWidth-mDeleteWidth,0);
        mHelper.smoothSlideViewTo(mContentView,-mDeleteWidth,0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private OnSweepListener mListener;

    public interface OnSweepListener{
        void onSweepChange(SweepView sweepView, boolean isOpen);
    }

    public void setOnSweepListener(OnSweepListener listener){
        mListener = listener;
    }
}
