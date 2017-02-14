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
import com.google.widget.view.ToggleButton;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/26 21:22
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class SwitchActivity extends AppCompatActivity {

    private ToggleButton mToggleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_switch);
        mToggleBtn = (ToggleButton) findViewById(R.id.toggle_btn);

        SpannableString title = new SpannableString("滑动开关");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    private void initListener() {
        mToggleBtn.setOnToggleChangeListener(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggleChange(boolean isOpen) {
                if (isOpen){
                    ToastUtil.toast("开关打开");
                }else {
                    ToastUtil.toast("开关关闭");
                }
            }
        });
    }

}
