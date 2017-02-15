package com.google.widget.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.SlidingMenu;

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
public class SlidingMenuActivity extends Activity {

    @Bind(R.id.slidingmenu)
    public SlidingMenu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_slidingmenu);
        ButterKnife.bind(this);
    }

    public void clickTab(View view){
        String text = ((TextView) view).getText().toString();
        ToastUtil.toast("点击了" + text);
        menu.toggle();
    }

    public void clickBack(View view){
        menu.toggle();
    }

}
