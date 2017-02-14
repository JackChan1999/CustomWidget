package com.google.widget.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.domain.Cheeses;
import com.google.widget.view.SwipeLayout;
import com.google.widget.view.SwipeLayout.OnSwipeStatusChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/29 14:57
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class SwipeActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview)
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_swipe);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new RvAdapter(this,R.layout.item_rv, Arrays.asList(Cheeses.NAMES)));
    }

    private class RvAdapter extends CommonAdapter<String>{
        List<SwipeLayout> opendItems;
        public RvAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
            opendItems = new ArrayList<>();
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.tv_name,s);
            ((SwipeLayout)holder.getView(R.id.sl)).setListener(new OnSwipeStatusChangeListener() {


                @Override
                public void onClose(SwipeLayout swipeLayout) {
                    opendItems.remove(swipeLayout);
                }

                @Override
                public void onOpen(SwipeLayout swipeLayout) {
                    opendItems.add(swipeLayout);
                }

                @Override
                public void onDragging(SwipeLayout swipeLayout) {

                }

                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {
                    for (SwipeLayout sl : opendItems){
                        sl.close();
                    }
                    opendItems.clear();
                }
            });
        }
    }
}
