package com.google.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.widget.animation.ResetAnimation;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/26 09:23
 * 描 述 ：视差特效
 * 修订历史 ：
 * ============================================================
 **/
public class ParallaxView extends ListView {

    private ImageView mImage;
    private int mOriginalHeight;//初始高度
    private int mIntrinsicHeight;//实际高度

    public ParallaxView(Context context) {
        super(context);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setParallaxImage(ImageView image){
        mImage = image;
        mOriginalHeight = image.getHeight();
        mIntrinsicHeight = image.getDrawable().getIntrinsicHeight();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
            scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
            isTouchEvent) {

        //在下拉的过程中，动态的改变headerView的高度
        if (isTouchEvent && deltaY < 0){
            if (mImage.getHeight() <= mIntrinsicHeight){
                int newHeight = (int) (mImage.getHeight() + Math.abs(deltaY/3.0f));
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)){
            case MotionEvent.ACTION_UP:
                //手指松开的时候，实现回弹的动画
                int startHeight = mImage.getHeight();
                int endHeight = mOriginalHeight;

                //实现方式1：自定义动画
                ResetAnimation animation = new ResetAnimation(mImage, startHeight, endHeight);
                startAnimation(animation);

                //实现方式2：ValueAnimator
                //valueAnimator(startHeight, endHeight);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                int newHeight = evaluate(fraction, startHeight, endHeight);
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        });
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 类型估值器
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }
}
