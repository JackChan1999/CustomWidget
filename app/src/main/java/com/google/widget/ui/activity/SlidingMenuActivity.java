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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/27 22:53
 * 描 述 ：
 * 修订历史 ：
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
