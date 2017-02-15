package com.google.widget.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.domain.Cheeses;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.MyDragLayout;
import com.google.widget.view.MyLinearLayout;

import java.util.Arrays;
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
public class DragActivity extends AppCompatActivity {

   /* @Bind(R.id.rv_left)
    RecyclerView rv_left;*/
    @Bind(R.id.rv_main)
    RecyclerView rv_main;
    @Bind(R.id.iv_header)
    ImageView mHeaderImage;
    @Bind(R.id.mll)
    MyLinearLayout mLinearLayout;
    @Bind(R.id.mdl)
    MyDragLayout mDragLayout;

    @Bind(R.id.tv_vip)
    TextView mTv_Vip;
    @Bind(R.id.tv_qianbao)
    TextView mTv_qianbao;
    @Bind(R.id.tv_zhuangban)
    TextView mTv_zhuangban;
    @Bind(R.id.tv_shoucang)
    TextView mTv_shoucang;
    @Bind(R.id.tv_xiangce)
    TextView mTv_xiangce;
    @Bind(R.id.tv_wenjian)
    TextView mTv_wenjian;
    @Bind(R.id.tv_businesscard)
    TextView mTv_businesscard;
    @Bind(R.id.night)
    TextView mTv_night;
    @Bind(R.id.setting)
    TextView mTv_setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        setAdapter();
    }

    private void initView() {
        setContentView(R.layout.activity_drag);
        ButterKnife.bind(this);
        mLinearLayout.setDragLayout(mDragLayout);
        initTextView();
        getSupportActionBar().hide();
    }

    private void initTextView() {
        Drawable vip = getResources().getDrawable(R.mipmap.vip);
        Rect rect = new Rect(0,0, (int) (vip.getIntrinsicWidth()*0.75f), (int) (vip.getIntrinsicHeight()*0.75));
        vip.setBounds(rect);
        mTv_Vip.setCompoundDrawables(vip,null,null,null);

        Drawable qianbao = getResources().getDrawable(R.mipmap.qianbao);
        qianbao.setBounds(rect);
        mTv_qianbao.setCompoundDrawables(qianbao,null,null,null);

        Drawable zhuangban = getResources().getDrawable(R.mipmap.zhuangban);
        zhuangban.setBounds(rect);
        mTv_zhuangban.setCompoundDrawables(zhuangban,null,null,null);

        Drawable shoucang = getResources().getDrawable(R.mipmap.shoucang);
        shoucang.setBounds(rect);
        mTv_shoucang.setCompoundDrawables(shoucang,null,null,null);

        Drawable xiangce = getResources().getDrawable(R.mipmap.xiangce);
        xiangce.setBounds(rect);
        mTv_xiangce.setCompoundDrawables(xiangce,null,null,null);

        Drawable wenjian = getResources().getDrawable(R.mipmap.wenjian);
        wenjian.setBounds(rect);
        mTv_wenjian.setCompoundDrawables(wenjian,null,null,null);

        Drawable businesscard = getResources().getDrawable(R.mipmap.businesscard);
        businesscard.setBounds(rect);
        mTv_businesscard.setCompoundDrawables(businesscard,null,null,null);

        Drawable setting = getResources().getDrawable(R.mipmap.setting);
        setting.setBounds(rect);
        mTv_setting.setCompoundDrawables(setting,null,null,null);

        Drawable night = getResources().getDrawable(R.mipmap.night);
        night.setBounds(rect);
        mTv_night.setCompoundDrawables(night,null,null,null);
    }

    private void initListener() {
        mDragLayout.setOnDragStatusChangeListener(new MyDragLayout.OnDragStatusChangeListener() {
            @Override
            public void onClose() {
                ToastUtil.toast("close");
                mHeaderImage.animate().translationX(15.0f).setInterpolator(new CycleInterpolator(4))
                        .setDuration(500).start();
            }

            @Override
            public void onOpen() {
                ToastUtil.toast("open");
                //rv_left.smoothScrollToPosition((int) Math.random() * 50);
            }

            @Override
            public void onDragging(float fraction) {
                ToastUtil.toast("dragging");
                mHeaderImage.setAlpha(1-fraction);
            }
        });
    }

    private void setAdapter() {
       /* LinearLayoutManager left_manager = new LinearLayoutManager(this);
        rv_left.setLayoutManager(left_manager);
        rv_left.setAdapter(new RvAdapter(this, R.layout.layout_left, Arrays.asList
                (Cheeses.sCheeseStrings)));*/

        LinearLayoutManager main_manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(main_manager);
        rv_main.setAdapter(new RvAdapter(this,R.layout.layout_main2,Arrays.asList
                (Cheeses.NAMES)));
    }

    private class RvAdapter extends CommonAdapter<String> {

        public RvAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.text, s);
        }
    }
}
