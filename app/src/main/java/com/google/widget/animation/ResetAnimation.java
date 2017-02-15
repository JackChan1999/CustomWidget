package com.google.widget.animation;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

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
 * des ：回弹动画
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
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
