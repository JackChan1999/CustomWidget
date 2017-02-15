package com.google.widget.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.abslistview.CommonAdapter;
import com.google.widget.factory.ThreadPoolFactory;
import com.google.widget.view.PullRefreshView;

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
public class PullRefreshActivity extends AppCompatActivity {

    private PullRefreshView mPullRefreshView;
    private List<String> list = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mMyAdapter.notifyDataSetChanged();
            mPullRefreshView.completeRefresh();
        }
    };
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setAdapter();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_pull_refresh);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh);
        mPullRefreshView.setCacheColorHint(Color.TRANSPARENT);
        mPullRefreshView.setFastScrollEnabled(true);
        mPullRefreshView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        SpannableString title = new SpannableString("下拉刷新,下拉加载");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    private void initData() {
        for (int i=0; i<15; i++){
            list.add("listview原来的数据 - "+i);
        }
    }

    private void setAdapter() {
        mMyAdapter = new MyAdapter(this, R.layout.list_item, list);
        mPullRefreshView.setAdapter(mMyAdapter);
    }

    private void initListener() {
        mPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore(boolean isLoadingMore) {
                requestDataFromServer(true);
            }
        });
    }

    private void requestDataFromServer(final boolean isLoadingMore){
        ThreadPoolFactory.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                if (isLoadingMore){
                    list.add("加载更多的数据-1");
                    list.add("加载更多的数据-2");
                    list.add("加载更多的数据-3");
                }else {
                    list.add(0, "下拉刷新的数据-3");
                    list.add(0, "下拉刷新的数据-2");
                    list.add(0, "下拉刷新的数据-1");
                }
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private class MyAdapter extends CommonAdapter<String>{

        public MyAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, String data) {
            holder.setText(R.id.tv_item,data);
        }
    }

}
