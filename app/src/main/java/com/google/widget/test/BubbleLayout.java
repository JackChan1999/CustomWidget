package com.google.widget.test;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

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
