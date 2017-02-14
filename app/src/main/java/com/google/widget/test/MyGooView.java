package com.google.widget.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.widget.utils.GeometryUtil;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/2 08:54
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class MyGooView extends View {

    //拖拽圆和固定圆的圆心和半径
    private PointF mDragCenter;
    private PointF mStickCenter;
    private PointF mInitCenter;

    private float mDragRadius;
    private float mStickRadius;
    private float mStickMinRadius;//固定圆最小半径
    private float mStickCurRadius = mStickRadius;//固定圆当前半径

    //拖拽范围
    private float farest;
    private float resetDistance;
    private boolean isOutofRange = false;
    private boolean isDisappear = false;

    //画笔，矩形，文本
    private Paint mPaintRed;
    private Paint mTextPaint;
    private Rect mRect;
    private String text = "";

    //动画
    private ValueAnimator mAnim;
    //状态栏高度
    private float mStatusBarHeight;

    private OnDisappearListener mListener;

    public MyGooView(Context context) {
        this(context, null);
    }

    public MyGooView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRect = new Rect(0,0,50,50);
        mDragRadius = 10;
        mStickRadius = 10;
        mStickMinRadius = 3;
        farest = 80;
        resetDistance = 40;

        mPaintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRed.setColor(Color.RED);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mDragRadius*1.2f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
    }

    public void setStatusBarHeight(float statusBarHeight) {
        mStatusBarHeight = statusBarHeight;
    }

    public void setStickRadius(float stickRadius) {
        mStickRadius = stickRadius;
    }

    public void setDragRadius(float dragRadius) {
        mDragRadius = dragRadius;
    }

    public void setText(int num) {
        this.text = String.valueOf(num);
    }

    public void initCenter(float x, float y){
        mDragCenter = new PointF(x, y);
        mStickCenter = new PointF(x, y);
        mInitCenter = new PointF(x, y);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0,-mStatusBarHeight);
        canvas.save();

        if (!isDisappear){
            if (!isOutofRange){
                ShapeDrawable drawable = drawGooView();
                drawable.setBounds(mRect);
                drawable.draw(canvas);
                canvas.drawCircle(mStickCenter.x,mStickCenter.y,mStickCurRadius,mPaintRed);
            }
            canvas.drawCircle(mDragCenter.x,mDragCenter.y,mDragRadius,mPaintRed);
            canvas.drawText(text,mDragCenter.x,mDragCenter.y+mDragRadius/2f,mTextPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isAnimRunning()){
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                if (isAnimRunning()){
                    return false;
                }
                isOutofRange = false;
                isDisappear = false;
                updateDragCenter(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                PointF p0 = new PointF(mDragCenter.x,mDragCenter.y);
                PointF p1 = new PointF(mStickCenter.x,mStickCenter.y);
                float distance = GeometryUtil.getDistanceBetween2Points(p0,p1);
                if (distance > farest){
                    isOutofRange = true;
                    updateDragCenter(event.getRawX(),event.getRawY());
                    return false;
                }
                updateDragCenter(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                break;
            default:
                isOutofRange = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleActionUp() {
        if (isOutofRange){
            if (GeometryUtil.getDistanceBetween2Points(mDragCenter,mStickCenter) < resetDistance){
                if (mListener != null){
                    mListener.onReset(isOutofRange);
                }
                return;
            }
            disappeared();
        }else {
            mAnim = ValueAnimator.ofFloat(1.0f);
            mAnim.setInterpolator(new OvershootInterpolator());

            final PointF startPoint = new PointF(mDragCenter.x,mDragCenter.y);
            final PointF endPoint = new PointF(mStickCenter.x,mStickCenter.y);
            mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    PointF dragCurCenter = GeometryUtil.getPointByPercent(startPoint,endPoint,fraction);
                    updateDragCenter(dragCurCenter.x, dragCurCenter.y);
                }
            });

            mAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mListener != null){
                        mListener.onReset(isOutofRange);
                    }
                }
            });

            if (GeometryUtil.getDistanceBetween2Points(startPoint,endPoint) < 10){
                mAnim.setDuration(10);
            }else {
                mAnim.setDuration(500);
            }
            mAnim.start();
        }
    }

    private void disappeared() {
        isDisappear = true;
        invalidate();
        if (mListener != null){
            mListener.onDisappear(mDragCenter);
        }
    }

    private void updateDragCenter(float x, float y) {
        mDragCenter.x = x;
        mDragCenter.y = y;
        invalidate();
    }

    public ShapeDrawable drawGooView(){
        Path path = new Path();
        //计算两圆圆心距离
        float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter,mStickCenter);
        //计算固定圆当前的半径
        mStickCurRadius = getCurrentRadius(distance);
        //计算两圆连接处的四个交点和贝塞尔曲线的控制点
        int xOffsert = (int) (mStickCenter.x - mDragCenter.x);
        Double slope = null;//斜率
        if (xOffsert != 0){
            slope = (double)(mStickCenter.y - mStickCenter.y)/xOffsert;
        }
        PointF[] dragPoints = GeometryUtil.getIntersectionPoints(mDragCenter,mDragRadius,slope);
        PointF[] stickPoints = GeometryUtil.getIntersectionPoints(mStickCenter,mStickCurRadius,slope);
        PointF controlPoint = GeometryUtil.getPointByPercent(mDragCenter,mStickCenter,0.618f);//控制点

        //画path
        path.moveTo(stickPoints[0].x, stickPoints[0].y);
        path.quadTo(controlPoint.x,controlPoint.y, dragPoints[0].x, dragPoints[0].y);
        path.lineTo(dragPoints[1].x, dragPoints[1].y);
        path.quadTo(controlPoint.x, controlPoint.y, stickPoints[1].x, stickPoints[1].y);
        path.close();

        ShapeDrawable drawable = new ShapeDrawable(new PathShape(path, 50,50));
        drawable.getPaint().setColor(Color.RED);
        return drawable;
    }

    /**获取固定圆当前的半径大小*/
    private float getCurrentRadius(float distance) {
        distance = Math.min(distance, farest);
        float fraction = (float) (0.2 + 0.8*distance/farest);
        return GeometryUtil.evaluateValue(fraction,mStickRadius,mStickMinRadius);
    }

    public boolean isAnimRunning(){
        if (mAnim != null && mAnim.isRunning()){
            return true;
        }
        return false;
    }

    //监听接口
    public interface OnDisappearListener{
        void onDisappear(PointF dragPoint);
        void onReset(boolean isOutofRange);
    }

    public void setListener(OnDisappearListener listener) {
        mListener = listener;
    }

    public OnDisappearListener getListener() {
        return mListener;
    }
}
