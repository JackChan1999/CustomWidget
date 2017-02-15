package com.google.widget.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.widget.R;
import com.google.widget.view.SpeedControlView;
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
public class SpeedControlActivity extends AppCompatActivity {

    private SpeedControlView speedControlView;
    private Button speedUp; //油门
    private Button speedDown;//刹车
    private Button shutDown; //手刹

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_control);
        speedControlView = (SpeedControlView) findViewById(R.id.speed_control);

        //实体化
        speedUp = (Button) findViewById(R.id.speed_up);
        speedDown = (Button) findViewById(R.id.speed_down);
        shutDown = (Button) findViewById(R.id.shut_down);

        //设置监听
        speedUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的时候加速
                        speedControlView.setType(1);
                        break;
                    case MotionEvent.ACTION_UP:
                        //松开做自然减速
                        speedControlView.setType(0);
                        break;
                }
                return true;
            }
        });
        speedDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的时候减速
                        speedControlView.setType(2);
                        break;
                    case MotionEvent.ACTION_UP:
                        //松开做自然减速
                        speedControlView.setType(0);
                        break;
                }
                return true;
            }
        });
        shutDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的时候拉手刹
                        speedControlView.setType(3);
                        break;
                    case MotionEvent.ACTION_UP:
                        //松开做自然减速
                        speedControlView.setType(0);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (speedControlView != null) {
            speedControlView.setSpeed(0);
            speedControlView.setStart(true);
        }
        new Thread(speedControlView).start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speedControlView != null) {
            speedControlView.setSpeed(0);
            speedControlView.setStart(false);
        }
    }
}
