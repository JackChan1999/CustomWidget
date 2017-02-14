package com.google.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/28 21:38
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class MyLinearLayout extends LinearLayout {

    private MyDragLayout mDragLayout;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragLayout(MyDragLayout dragLayout){
        mDragLayout = dragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragLayout.getStatus() == MyDragLayout.Status.Close){
            return super.onInterceptTouchEvent(ev);
        }else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragLayout.getStatus() == MyDragLayout.Status.Close){
            return super.onTouchEvent(event);
        }else {
            if (event.getAction() == MotionEvent.ACTION_UP){
                mDragLayout.close();
            }
            return true;
        }
    }
}
