package com.google.animdemo;

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

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/5 13:39
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class PageTabStrip extends HorizontalScrollView {

    private int mTabWidth;
    private RadioGroup mRadioGroup;
    private ImageView mIvIndicator;
    private String[] mTitle;
    private float mCurrentIndicatorLeft;
    private float mScrollX;
    private ColorStateList mColorStateList;
    private RadioGroup.LayoutParams mParams;

    public PageTabStrip(Context context) {
        this(context, null);
    }

    public PageTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(String[] title, int count, int width){
        mTitle = title;
        mTabWidth = width/count;
        initView();
        addRadioButton();
        initListener();
    }

    private void initView() {
        View child =  LayoutInflater.from(getContext()).inflate(R.layout.layout_page_tab_strip,this,true);
        mRadioGroup = (RadioGroup) child.findViewById(R.id.rg_nav_content);
        mIvIndicator = (ImageView) child.findViewById(R.id.iv_nav_indicator);

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
        for (int i=0; i<mTitle.length; i++){
            mRadioGroup.addView(initRadioButton(i));
        }
    }

    private RadioButton initRadioButton(int position) {
        RadioButton rb = new RadioButton(getContext());
        rb.setId(position);
        rb.setText(mTitle[position]);
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
