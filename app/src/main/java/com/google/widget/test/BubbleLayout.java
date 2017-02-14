package com.google.widget.test;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/1 17:40
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class BubbleLayout extends FrameLayout {

    public BubbleLayout(Context context) {
        super(context);
    }

    private int currentX, currentY;

    public void setCenter(int x, int y) {
        currentX = x;
        currentY = y;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View child = getChildAt(0);
        if (child != null && child.getVisibility() != View.GONE) {
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            child.layout((int) (currentX - width / 2f), (int) (currentY - height / 2f), (int)
                    (currentX + width / 2f), (int) (currentY + height / 2f));
        }
    }
}
