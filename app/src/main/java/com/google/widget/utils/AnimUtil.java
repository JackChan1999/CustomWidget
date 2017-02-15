package com.google.widget.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

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
public class AnimUtil {

    public static int animCount = 0;//记录当前执行的动画数量

    public static void closeMenu(View view, int startOffset) {
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());
        //view.invalidate();
        view.animate().rotation(-180).setDuration(500).setListener(mListener).setStartDelay
                (startOffset).start();
    }

    public static void openMenu(View view, int startOffset) {
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());
        //view.invalidate();
        view.animate().rotation(0).setDuration(500).setListener(mListener).setStartDelay
                (startOffset).start();
    }

    static AnimatorListener mListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationStart(Animator animation) {
            animCount++;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animCount--;
        }
    };
}
