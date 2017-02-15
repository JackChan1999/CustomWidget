package com.google.widget.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.base.adapter.recyclerview.OnItemClickListener;
import com.google.widget.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

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
public class CustomSpinnerActivity extends AppCompatActivity {

    @Bind(R.id.et_spinner)
    public EditText mEditText;
    @Bind(R.id.ib_select)
    public ImageButton mButton;
    private List<String> list;
    private SpinnerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private PopupWindow mPopupWindow;
    private int popupWindowHeight = UIUtil.dip2px(300);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberList();
            }
        });
    }

    private void showNumberList() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mRecyclerView, mEditText.getWidth(), popupWindowHeight);
        }
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(mEditText, 0, UIUtil.dip2px(10));
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            list.add(1880191110 + i + "");
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setBackgroundResource(R.mipmap.listview_background);
        mRecyclerView.setVerticalScrollBarEnabled(false);
        mAdapter = new SpinnerAdapter(this, R.layout.adapter_list, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mEditText.setText(list.get(position));
                mPopupWindow.dismiss();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_spinner);
        ButterKnife.bind(this);
        list = new ArrayList<>();

        SpannableString title = new SpannableString("下拉选择");
        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        SpannableString hint = new SpannableString("选择联系人");
        hint.setSpan(new AbsoluteSizeSpan(16, true), 0, hint.length(), Spannable
                .SPAN_INCLUSIVE_EXCLUSIVE);
        hint.setSpan(new ForegroundColorSpan(Color.parseColor("#9e9e9e")), 0, hint.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mEditText.setText(hint);
    }

    private class SpinnerAdapter extends CommonAdapter<String> {

        public SpinnerAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, String s) {
            holder.setText(R.id.tv, s);
            holder.setOnClickListener(R.id.ib_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(holder.getPos());
                    notifyDataSetChanged();
                    int recyclerviewHeight = holder.getConvertView().getHeight() * list.size();
                    mPopupWindow.update(mEditText.getWidth(), recyclerviewHeight >
                            popupWindowHeight ? popupWindowHeight : recyclerviewHeight);
                    if (list.size() == 0) {
                        mPopupWindow.dismiss();
                        mButton.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
