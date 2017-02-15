package com.google.widget.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.widget.R;
import com.google.widget.utils.Cheeses;
import com.google.widget.view.ParallaxView;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class ParallaxActivity extends AppCompatActivity {

    @Bind(R.id.parallaxView)
    public ParallaxView mParallaxView;
    private View mHeaderView;
    private ImageView mImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_pallarax);
        ButterKnife.bind(this);
        mParallaxView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        SpannableString title = new SpannableString("视差特效");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        mHeaderView = LayoutInflater.from(this).inflate(R.layout.parallax_header, null);
        mImage = (ImageView) mHeaderView.findViewById(R.id.iv);
        mParallaxView.addHeaderView(mHeaderView);

        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mParallaxView.setParallaxImage(mImage);
                mParallaxView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        mParallaxView.setAdapter(new ArrayAdapter<String>(this, android.R.layout
                .simple_list_item_1, Cheeses.NAMES));
    }
}
