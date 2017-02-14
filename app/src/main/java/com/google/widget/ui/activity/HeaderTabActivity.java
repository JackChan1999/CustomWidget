package com.google.widget.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.widget.R;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.HeaderTab;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/31 18:39
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class HeaderTabActivity extends AppCompatActivity {

    private HeaderTab mTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_tab);
        mTab = (HeaderTab) findViewById(R.id.tab);
        mTab.bindTab(mCallback,"消息","电话");
        mTab.leftClick();

        SpannableString title = new SpannableString("HeaderTab");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    private HeaderTab.ITabActionCallback mCallback = new HeaderTab.ITabActionCallback() {
        @Override
        public void onLeftTabClick() {
            ToastUtil.toast("消息");
        }

        @Override
        public void onRightTabClick() {
            ToastUtil.toast("电话");
        }
    };
}
