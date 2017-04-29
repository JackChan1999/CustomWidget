package com.google.widget.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.view.SweepView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

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
public class SweepActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview)
    public RecyclerView mRecyclerView;

    private List<String> data;
    // 会发生并发修改异常，118行遍历的时候，100行删除，使用CopyOnWriteArrayList
    // private List<SweepView> mOpenedViews = new ArrayList<>();
    private CopyOnWriteArrayList<SweepView> mOpenedViews = new CopyOnWriteArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setAdapter();
    }

    private void initView() {
        setContentView(R.layout.activity_sweep);
        ButterKnife.bind(this);
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i=0; i<200; i++){
            data.add("内容--" + i);
        }
    }

    private void setAdapter() {
        SweepAdapter adapter = new SweepAdapter(this,R.layout.sweep_item,data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    private class SweepAdapter extends CommonAdapter<String>{

        public SweepAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final String s) {
            holder.setText(R.id.item_tv_content,s);

            ((SweepView)holder.getView(R.id.item_sv)).setOnSweepListener(new SweepView.OnSweepListener() {

                @Override
                public void onSweepChange(SweepView sweepView, boolean isOpen) {
                    if (isOpen){
                        if (!mOpenedViews.contains(sweepView)){
                            mOpenedViews.add(sweepView);
                        }
                    }else {
                        mOpenedViews.remove(sweepView);
                    }
                }
            });

            holder.setOnClickListener(R.id.item_tv_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(s);
                    closeAll();
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void closeAll() {
        ListIterator<SweepView> iterator = mOpenedViews.listIterator();
        while (iterator.hasNext()){
            SweepView sweepView = iterator.next();
            sweepView.close();
        }
    }
}
