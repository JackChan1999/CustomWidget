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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/30 20:14
 * 描 述 ：
 * 修订历史 ：
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
