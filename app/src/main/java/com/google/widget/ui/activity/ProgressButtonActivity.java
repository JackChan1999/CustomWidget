package com.google.widget.ui.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.widget.R;
import com.google.widget.view.ProgressButton;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/30 18:35
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class ProgressButtonActivity extends AppCompatActivity {

    private ProgressButton mButton;
    private boolean isStop = false;
    private int progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbutton);
        mButton = (ProgressButton) findViewById(R.id.pb);
        mButton.setBackgroundResource(R.drawable.progress_normal);
        mButton.setProgressEnable(true);

        SpannableString title = new SpannableString("ProgressButton");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        initListener();
    }

    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStop = !isStop;
                new AsyncTask<Void,Integer,Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        while (isStop){
                            SystemClock.sleep(100);
                            if (progress >= 100){
                                progress = 0 ;
                                break;
                            }
                            progress++;
                            publishProgress(progress);
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        int progress = values[0];
                        mButton.setProgress(progress);
                        mButton.setText(progress+"%");
                    }
                }.execute();
            }
        });
    }
}
