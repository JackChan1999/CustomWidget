package com.google.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
public class RingWave extends View {

    private List<Wave> mWaveList = new ArrayList<>();
    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            flushData();
            invalidate();
            if (!mWaveList.isEmpty()){
                mHandler.sendEmptyMessageDelayed(0,50);
            }
        }
    };

    private void flushData() {
        ArrayList<Wave> removeList = new ArrayList<>();
        for (Wave wave : mWaveList){
            wave.radius += 3;
            wave.paint.setStrokeWidth(wave.radius/3);

            if (wave.paint.getAlpha() < 0){
                removeList.add(wave);
                continue;
            }

            int alpha = wave.paint.getAlpha();
            alpha -= 5;
            if (alpha < 0) alpha = 0;

            wave.paint.setAlpha(alpha);
        }
        mWaveList.removeAll(removeList);
    }


    public RingWave(Context context) {
        this(context, null);
    }

    public RingWave(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Wave wave : mWaveList){
            canvas.drawCircle(wave.cx, wave.cy, wave.radius, wave.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                addPoint((int)event.getX(), (int)event.getY());
                break;
        }
        return true;
    }

    private void addPoint(int x, int y) {
        if (mWaveList.isEmpty()){
            addWave(x, y);
            mHandler.sendEmptyMessageDelayed(0,50);
        }else {
            Wave lastWave = mWaveList.get(mWaveList.size() - 1);
            if (Math.abs(x - lastWave.cx) > 10 || Math.abs(y - lastWave.cy) > 10){
                addWave(x, y);
            }
        }
    }

    private void addWave(int x, int y) {
        Wave wave = new Wave();
        wave.cx = x;
        wave.cy = y;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(wave.radius/3);
        paint.setAlpha(255);
        paint.setAntiAlias(true);

        int colorIndex = (int) (Math.random()*4);
        paint.setColor(colors[colorIndex]);

        wave.paint = paint;

        mWaveList.add(wave);
    }

    private class Wave{
        public int cx;
        public int cy;
        public int radius;
        public Paint paint;
    }
}
