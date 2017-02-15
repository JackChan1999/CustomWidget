package com.google.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

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
public class ProgressButton extends Button {

    private boolean mProgressEnable;
    private long mMax = 100;
    private long mProgress;
    private Drawable mProgressDrawable;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgressDrawable = new ColorDrawable(Color.parseColor("#5677fc"));
    }
    /**设置是否允许进度*/
    public void setProgressEnable(boolean progressEnable) {
        mProgressEnable = progressEnable;
    }
    /**设置进度的最大值*/
    public void setMax(long max) {
        mMax = max;
    }
    /**设置当前进度，并重绘*/
    public void setProgress(long progress) {
        mProgress = progress;
        invalidate();
    }
    /**设置ProgressButton的进度图片*/
    public void setProgressDrawable(Drawable progressDrawable) {
        mProgressDrawable = progressDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgressEnable){
            int left = 0;
            int top = 0;
            int right = (int) (mProgress*1.0f/mMax*getMeasuredWidth()+.5f);
            int bottom = getBottom();
            mProgressDrawable.setBounds(left,top,right,bottom);//设置绘制的范围
            mProgressDrawable.draw(canvas);
        }
        super.onDraw(canvas);//绘制文本和背景
    }
}
