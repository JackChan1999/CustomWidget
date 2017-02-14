package com.google.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.widget.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/30 19:48
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class CircleProgressButton extends LinearLayout {

    private ImageView mIcon;
    private TextView mNote;
    private boolean mProgressEnable;
    private long mMax = 360;
    private long mProgress;
    private Paint mPaint;

    public CircleProgressButton(Context context) {
        this(context, null);
    }

    public CircleProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_circleprogress, this);
        mIcon = (ImageView) view.findViewById(R.id.cp_icon);
        mNote = (TextView) findViewById(R.id.cp_note);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    public void setIcon(ImageView icon) {
        mIcon = icon;
    }

    public void setNote(String note) {
        mNote.setText(note);
    }

    public void setProgressEnable(boolean progressEnable) {
        mProgressEnable = progressEnable;
    }

    public void setMax(long max) {
        mMax = max;
    }

    public void setProgress(long progress) {
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//绘制背景，透明图片，设置背景时才会调用此方法
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);//绘制具体内容（文本和图片）
        if (mProgressEnable) {
            RectF oval = new RectF(mIcon.getLeft(), mIcon.getTop(), mIcon.getRight(), mIcon
                    .getBottom());
            float startAngle = -90;
            float sweepAngle = mProgress*360.0f/mMax;
            boolean useCenter = false;
            canvas.drawArc(oval,startAngle,sweepAngle,useCenter,mPaint);
        }
    }
}
