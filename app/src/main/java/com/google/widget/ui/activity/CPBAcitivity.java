package com.google.widget.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.widget.R;
import com.google.widget.view.CircleProgressButton;

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
public class CPBAcitivity extends AppCompatActivity {

    private CircleProgressButton mCPBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circleprogress);
        mCPBtn = (CircleProgressButton) findViewById(R.id.cpb);
        mCPBtn.setProgressEnable(true);
        mCPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void,Integer,Void>(){
                    int i = 0;
                    @Override
                    protected Void doInBackground(Void... params) {
                        while (true){
                            SystemClock.sleep(10);
                            publishProgress(i);
                            if (i > 360){
                                break;
                            }
                            i++;
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                       int i = values[0];
                        mCPBtn.setProgress(i);
                        mCPBtn.setNote((int)(i*100.0f/360)+"%");
                    }
                }.execute();
            }
        });
    }
}
