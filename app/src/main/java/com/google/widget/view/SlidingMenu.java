package com.google.widget.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

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
public class SlidingMenu extends ViewGroup {

    private View mMenuView;
    private View mMainView;
    private int mMenuWidth;
    private Scroller mScroller;

    private int touchSlop;
    private float downX;

    private boolean isMenuShow = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public boolean isMenuShow(){
        return isMenuShow;
    }

    public void toggle(){
        switchMenu(!isMenuShow);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
        mMenuWidth = mMenuView.getLayoutParams().width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量左侧
        int menuWidthSpec = MeasureSpec.makeMeasureSpec(mMenuWidth, MeasureSpec.EXACTLY);
        mMenuView.measure(menuWidthSpec,heightMeasureSpec);
        //测量右侧
        mMainView.measure(widthMeasureSpec, heightMeasureSpec);
        //设置自身宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuView.layout(-mMenuWidth,0,0,b);
        mMainView.layout(l,t,r,b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)){
            case MotionEvent.ACTION_DOWN:
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
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                int dx = (int) (downX - moveX + 0.5f);
                int scrollX = getScrollX() + dx;
                if (scrollX < 0 && scrollX < -mMenuWidth){
                    scrollTo(-mMenuWidth, 0);
                }else if (scrollX > 0){
                    scrollTo(0,0);
                }else {
                    scrollBy(dx,0);
                }
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                switchMenu(getScrollX() <= -mMenuWidth/2);
                break;
        }
        return true;
    }

    private void switchMenu(boolean showMenu){
        isMenuShow = showMenu;
        int startX = getScrollX();
        int dx = 0;
        if (!showMenu){
            dx = -startX;
        }else {
            dx = -mMenuWidth - startX;
        }
        mScroller.startScroll(startX,0,dx,0,Math.abs(dx));
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
