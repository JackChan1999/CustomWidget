package com.google.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.widget.utils.GeometryUtil;
import com.google.widget.utils.UIUtil;
import com.google.widget.utils.Utils;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
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

/**
 * 粘性控件
 * @author poplar
 *
 */
public class MyGooView extends View {

    private static final String TAG = "TAG";
    private Paint mPaint;
    private PointF[] mStickPoints;
    private PointF[] mDragPoints;
    private PointF mControlPoint;//控制点
    private PointF mDragCenter;//拖拽圆圆心
    private PointF mStickCenter;//固定圆圆心
    private float mDragRadius;//拖拽圆半径
    private float mStickRadius;//固定圆半径
    private float farestDistance;//拖拽断开的最大范围
    private int statusBarHeight;
    private boolean isOutofRange;
    private boolean isDisappear;

    public MyGooView(Context context) {
        this(context, null);
    }

    public MyGooView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MyGooView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mDragCenter = new PointF(376f, 760f);
        mStickCenter = new PointF(576f, 960f);
        mDragRadius = UIUtil.dip2px(17);
        mStickRadius = UIUtil.dip2px(15);
        farestDistance = UIUtil.dip2px(90);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 计算连接点值, 控制点, 固定圆半径

        // 1. 获取固定圆半径(根据两圆圆心距离)
        float tempStickRadius = getTempStickRadius();

        // 2. 获取直线与圆的交点
        float yOffset = mStickCenter.y - mDragCenter.y;
        float xOffset = mStickCenter.x - mDragCenter.x;
        Double lineK = null;
        if(xOffset != 0){
            lineK = (double) (yOffset / xOffset);
        }
        // 通过几何图形工具获取交点坐标
        mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
        mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter, tempStickRadius, lineK);

        // 3. 获取控制点坐标
        mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);


        // 保存画布状态
        canvas.save();
        canvas.translate(0, -statusBarHeight);

        // 画出最大范围(参考用)
        mPaint.setStyle(Style.STROKE);
        canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, mPaint);
        mPaint.setStyle(Style.FILL);

        if(!isDisappear){
            if(!isOutofRange){
                // 3. 画连接部分
                Path path = new Path();
                // 跳到点1
                path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
                // 画曲线1 -> 2
                path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x, mDragPoints[0].y);
                // 画直线2 -> 3
                path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
                // 画曲线3 -> 4
                path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
                path.close();
                canvas.drawPath(path, mPaint);

                // 画附着点(参考用)
                mPaint.setColor(Color.BLUE);
                canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 3f, mPaint);
                canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 3f, mPaint);
                canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 3f, mPaint);
                canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 3f, mPaint);
                mPaint.setColor(Color.RED);

                // 2. 画固定圆
                canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempStickRadius, mPaint);
            }

            // 1. 画拖拽圆
            canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, mPaint);
        }

        // 恢复上次的保存状态
        canvas.restore();
    }

    // 获取固定圆半径(根据两圆圆心距离)
    private float getTempStickRadius() {
        float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);

        //		if(distance> farestDistance){
        //			distance = farestDistance;
        //		}
        distance = Math.min(distance, farestDistance);

        // 0.0f -> 1.0f
        float percent = distance / farestDistance;
        Log.d(TAG, "percent: " + percent);

        // percent , 100% -> 20%
        return evaluate(percent, mStickRadius, mStickRadius * 0.5f);
    }

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOutofRange = false;
                isDisappear = false;
                x = event.getRawX();
                y = event.getRawY();
                updateDragCenter(x, y);

                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                updateDragCenter(x, y);

                // 处理断开事件
                float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                if(distance > farestDistance){
                    isOutofRange = true;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if(isOutofRange){
                    float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                    if(d > farestDistance){
                        // a. 拖拽超出范围,断开, 松手, 消失
                        isDisappear = true;
                        invalidate();
                    }else {
                        //b. 拖拽超出范围,断开,放回去了,恢复
                        updateDragCenter(mStickCenter.x, mStickCenter.y);
                    }

                }else {
                    //				c. 拖拽没超出范围, 松手,弹回去
                    final PointF tempDragCenter = new PointF(mDragCenter.x, mDragCenter.y);

                    ValueAnimator mAnim = ValueAnimator.ofFloat(1.0f);
                    mAnim.addUpdateListener(new AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator mAnim) {
                            // 0.0 -> 1.0f
                            float percent = mAnim.getAnimatedFraction();
                            PointF p = GeometryUtil.getPointByPercent(tempDragCenter, mStickCenter, percent);
                            updateDragCenter(p.x, p.y);
                        }
                    });
                    mAnim.setInterpolator(new OvershootInterpolator(4));
                    mAnim.setDuration(500);
                    mAnim.start();
                }

                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 更新拖拽圆圆心坐标,并重绘界面
     * @param x
     * @param y
     */
    private void updateDragCenter(float x, float y) {
        mDragCenter.set(x, y);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        statusBarHeight = Utils.getStatusBarHeight(this);
    }

}
