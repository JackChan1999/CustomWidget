package com.google.widget.animation;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/26 09:34
 * 描 述 ：回弹动画
 * 修订历史 ：
 * ============================================================
 **/
public class ResetAnimation extends Animation {

    private ImageView mImage;
    private int startHeight;
    private int endHeight;

    public ResetAnimation(ImageView image, int startHeight, int endHeight){
        this.mImage = image;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        setInterpolator(new OvershootInterpolator());
        setDuration(500);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        int newHeight = evaluate(interpolatedTime, startHeight, endHeight);
        mImage.getLayoutParams().height = newHeight;
        mImage.requestLayout();
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
