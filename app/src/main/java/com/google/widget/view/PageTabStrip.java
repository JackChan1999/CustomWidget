package com.google.widget.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.widget.R;

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
public class PageTabStrip extends HorizontalScrollView {

    private int mTabWidth;
    private float mCurrentIndicatorLeft;
    private float mScrollX;
    private String[] mTabText;

    private RadioGroup mRadioGroup;
    private ImageView mIvIndicator;
    private CustomViewPager mPager;
    private ColorStateList mColorStateList;
    private RadioGroup.LayoutParams mParams;

    public PageTabStrip(Context context) {
        this(context, null);
    }

    public PageTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(CustomViewPager pager, String[] tabText, int count, int width){
        mTabText = tabText;
        mTabWidth = width/count;
        mPager = pager;
        initView();
        addRadioButton();
        initListener();
    }

    public void check(int position){
        mRadioGroup.check(position);
    }

    private void initView() {
        View child =  LayoutInflater.from(getContext()).inflate(R.layout.layout_page_tab_strip,this,true);
        mRadioGroup = (RadioGroup) child.findViewById(R.id.rg);
        mIvIndicator = (ImageView) child.findViewById(R.id.iv_index);

        ViewGroup.LayoutParams params = mIvIndicator.getLayoutParams();
        params.width = mTabWidth;
        mIvIndicator.setLayoutParams(params);

        mColorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked},
                new int[]{}}, new int[]{Color.WHITE,Color.BLACK});

        mParams = new RadioGroup.LayoutParams(mTabWidth, RadioGroup.LayoutParams.MATCH_PARENT);

        setHorizontalScrollBarEnabled(false);
    }

    private void addRadioButton() {
        mRadioGroup.removeAllViews();
        for (int i = 0; i< mTabText.length; i++){
            mRadioGroup.addView(initRadioButton(i));
        }
    }

    private RadioButton initRadioButton(int position) {
        RadioButton rb = new RadioButton(getContext());
        rb.setId(position);
        rb.setText(mTabText[position]);
        rb.setTextColor(mColorStateList);
        rb.setButtonDrawable(android.R.color.transparent);
        rb.setTextSize(16);
        rb.setLayoutParams(mParams);
        rb.setGravity(Gravity.CENTER);
        if (position == 0) {
            rb.setChecked(true);
        }
        return rb;
    }

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                moveAnimation(checkedId);
                mPager.setCurrentItem(checkedId,false);
            }
        });
    }

    private void moveAnimation(int checkedId) {
        mIvIndicator.animate().translationX(mTabWidth*checkedId).setInterpolator(
                new LinearInterpolator()).setDuration(100).start();
        mCurrentIndicatorLeft = mTabWidth*checkedId;
        mScrollX = (checkedId > 1 ? mCurrentIndicatorLeft: 0)- mTabWidth * 2;
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo((int) mScrollX,0);
            }
        });
    }

}
