package com.google.widget.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.widget.R;
import com.google.widget.view.QQHealthView;

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
public class QQHealthViewActivity extends AppCompatActivity {

    private QQHealthView view;
    public static List<Integer> sizes = new ArrayList<>();
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        initview();
    }

    private void initview() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QQHealthView");
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1e95ef")));

        view = (QQHealthView) findViewById(R.id.QQView);
        view.setMySize(2345);
        view.setRank(11);
        view.setAverageSize(5436);
        sizes.add(1234);
        sizes.add(2234);
        sizes.add(4234);
        sizes.add(6234);
        sizes.add(3834);
        sizes.add(7234);
        sizes.add(5436);
        btn = (Button) findViewById(R.id.set_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.reSet(6534, 8, 4567);
            }
        });
    }
}
