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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/30 18:15
 * 描 述 ：
 * 修订历史 ：
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
