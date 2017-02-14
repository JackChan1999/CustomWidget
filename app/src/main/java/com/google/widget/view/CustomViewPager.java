package com.google.widget.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/26 09:16
 * 描 述 ：自定义ViewGroup，实现ViewPager的功能
 * 修订历史 ：
 * ============================================================
 **/
public class CustomViewPager extends ViewGroup {

    private GestureDetector mDetector;
    private Scroller mScroller;
    private int touchSlop;
    private OnPageChangeListener mListener;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mScroller = new Scroller(getContext());

        mDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float
                    distanceY) {
                scrollBy((int) distanceX,0);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1. 测量每个孩子的宽高
        for (int i=0; i<getChildCount(); i++){
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
        //2. 设置自己的宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
       setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i=0; i<getChildCount(); i++){
            getChildAt(i).layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
        }
    }

    private float downX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)){
            case MotionEvent.ACTION_DOWN:
                mDetector.onTouchEvent(ev);
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - downX) > touchSlop){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);//将touch交给手势识别器处理
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int pos = (getScrollX() + getWidth()/2)/getWidth();
                if (pos > getChildCount() -1){
                    pos = getChildCount() -1;
                }
                setCurrentItem(pos,true);
                break;
        }
        return true;
    }

    public void setCurrentItem(int pos, boolean isSmooth) {
        if (isSmooth){
            int distance = pos*getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(),0,distance,0,Math.abs(distance));
            ViewCompat.postInvalidateOnAnimation(this);
            if (mListener != null){
                mListener.onPagerSelected(pos);
            }
        }else {
            scrollTo(pos*getWidth(),0);
        }

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), 0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface OnPageChangeListener {
        void onPagerSelected(int pos);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener){
        mListener = listener;
    }
}
