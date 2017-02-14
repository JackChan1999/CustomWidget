package com.google.widget.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/22 20:44
 * 描 述 ：
 * 修订历史 ：
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
