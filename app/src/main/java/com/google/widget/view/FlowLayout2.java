package com.google.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

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
public class FlowLayout2 extends ViewGroup {

    public static final int DEFAULT_SPACING = 25;
    private int mWidth;
    private int horizontalSpace = DEFAULT_SPACING;
    private int verticalSpace = DEFAULT_SPACING;
    private List<Line> mLines;//记录所有行
    private Line currentLine;//记录当前行
    private int usedWidth = 0;//记录当前行使用的宽度

    public FlowLayout2(Context context) {
        this(context, null);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLines = new ArrayList<>();
    }

    public void setHorizontalSpace(int horizontalSpace){
        this.horizontalSpace = horizontalSpace;
    }

    public void setVerticalSpace(int verticalSpace){
        this.verticalSpace = verticalSpace;
    }

    public void setSpace(int horizontalSpace, int verticalSpace){
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取flowlayout的size和mode
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();

        //组装子view的spec
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);

        restoreLine();// 还原数据，以便重新记录

        //遍历子view，测量每个子view的宽高
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE){
                continue;
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            if (currentLine == null){
                currentLine = new Line();
            }
            usedWidth += child.getMeasuredWidth();
            if (usedWidth <= mWidth) {
                currentLine.addChild(child);
                usedWidth += horizontalSpace;
                if (usedWidth > mWidth) {
                    newLine();//换行
                }
            } else {
                if (currentLine.getChildCount() < 1) {
                    currentLine.addChild(child);
                }
                newLine();
            }
        }
        //添加最后一行
        if (!mLines.contains(currentLine)) {
            mLines.add(currentLine);
        }

        int totalHeight = getPaddingBottom() + getPaddingTop() + verticalSpace * (mLines.size()-1);
        for (Line line : mLines) {
            totalHeight += line.getHeight();
        }

        //设置自己的宽高
        setMeasuredDimension(mWidth + getPaddingLeft() + getPaddingRight(), resolveSize
                (totalHeight, heightMeasureSpec));
    }
    /** 还原所有数据 */
    private void restoreLine(){
        mLines.clear();
        usedWidth = 0;
        currentLine = new Line();
    }

    /**换行*/
    private void newLine() {
        mLines.add(currentLine);
        currentLine = new Line();
        usedWidth = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t += getPaddingTop();
        for (Line line : mLines) {
            line.layout(l, t);
            t += verticalSpace + line.getHeight();
        }
    }

    /**
     * 代表着一行，封装了一行所占高度，该行子View的集合，以及所有View的宽度总和
     */
    private class Line {

        private int height;
        private int lineWidth;
        private List<View> children = new ArrayList<>();

        public int getHeight() {
            return height;
        }

        public int getChildCount() {
            return children.size();
        }

        public void addChild(View child) {
            children.add(child);
            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();
            }
            lineWidth += child.getMeasuredWidth();
        }

        public void layout(int left, int top) {
            int surplusWidth = mWidth - lineWidth - horizontalSpace * (children.size() - 1);
            int widthAvg = 0;
            if (surplusWidth > 0) {
                widthAvg = surplusWidth / children.size();
            }
            for (View child : children) {
                if (widthAvg > 0){
                    int newWidth = child.getMeasuredWidth() + widthAvg;
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth,MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(),MeasureSpec.EXACTLY);
                    child.measure(widthMeasureSpec,heightMeasureSpec);
                }
                child.layout(left, top, left + child.getMeasuredWidth(), top +
                        child.getMeasuredHeight());
                left += child.getMeasuredWidth() + horizontalSpace;
            }
        }
    }
}
