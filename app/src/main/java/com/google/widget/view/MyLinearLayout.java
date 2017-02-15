package com.google.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

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
