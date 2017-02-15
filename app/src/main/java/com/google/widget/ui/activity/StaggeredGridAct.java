package com.google.widget.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.widget.R;

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
public class StaggeredGridAct extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.layout_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //mRefreshLayout.setProgressBackgroundColorSchemeColor(0xff67bf74);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.user1);
        Bitmap b = createRoundConerImage(bitmap);
        mToolbar.setNavigationIcon(new BitmapDrawable(b));
        setSupportActionBar(mToolbar);

        initStaggeredGridView(true);
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
    }

    private View initStaggeredGridView(boolean isVer) {
        mToolbar.setSubtitle(isVer ?"StaggeredGridLayoutManager Vertical":"StaggeredGridLayoutManager Horizontal");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,isVer ?
                LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new StaggeredGridAdapter(this,isVer));
        return mRecyclerView;
    }

    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        int size = Math.min(source.getWidth(), source.getHeight());

        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, size, size);
        canvas.drawRoundRect(rect, size / 2, size / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.ListHolder>{

        Context context;
        boolean flag;
        int iconsV[] = {R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5, R.mipmap
                .p6, R.mipmap.p7, R.mipmap.p9,
                R.mipmap.p10, R.mipmap.p11, R.mipmap.p12, R.mipmap.p13, R.mipmap.p14, R.mipmap.p15, R.mipmap.p16, R.mipmap.p17, R.mipmap.p18, R.mipmap.p19,
                R.mipmap.p20, R.mipmap.p21, R.mipmap.p22, R.mipmap.p23, R.mipmap.p24, R.mipmap.p25, R.mipmap.p26, R.mipmap.p27, R.mipmap.p28, R.mipmap.p29,
                R.mipmap.p30, R.mipmap.p31, R.mipmap.p32, R.mipmap.p33, R.mipmap.p34, R.mipmap.p35, R.mipmap.p36, R.mipmap.p37, R.mipmap.p38, R.mipmap.p39,

                R.mipmap.p40, R.mipmap.p41, R.mipmap.p42, R.mipmap.p43, R.mipmap.p44};

        int iconsH[] = {R.mipmap.h1, R.mipmap.h2, R.mipmap.h3, R.mipmap.h4, R.mipmap.h5, R.mipmap.h6, R.mipmap.h7, R.mipmap.h9,
                R.mipmap.h10, R.mipmap.h11, R.mipmap.h12, R.mipmap.h13, R.mipmap.h14, R.mipmap.h15, R.mipmap.h16, R.mipmap.h17, R.mipmap.h18, R.mipmap.h19,
                R.mipmap.h20, R.mipmap.h21, R.mipmap.h22, R.mipmap.h23, R.mipmap.h24, R.mipmap.h25, R.mipmap.h26, R.mipmap.h27, R.mipmap.h28, R.mipmap.h29,
                R.mipmap.h30, R.mipmap.h31, R.mipmap.h32, R.mipmap.h33, R.mipmap.h34, R.mipmap.h35, R.mipmap.h36, R.mipmap.h37, R.mipmap.h38, R.mipmap.h39,
                R.mipmap.h40, R.mipmap.h41, R.mipmap.h42, R.mipmap.h43, R.mipmap.h44};
        public StaggeredGridAdapter(Context context, boolean flag){
            this.context = context;
            this.flag = flag;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(flag ? R.layout.staggered_grid_item : R.layout.staggered_grid_item_h, parent, false);
            return new ListHolder(view);
        }


        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return 1000;
        }


        class ListHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView name;

            public ListHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.pic);
                name = (TextView) itemView.findViewById(R.id.name);
            }

            public void setData(int position){
                if (flag) {
                    icon.setImageResource(iconsV[position % iconsV.length]);
                } else {
                    icon.setImageResource(iconsH[position % iconsH.length]);
                }
                name.setText("This is position "+ position);
            }


        }
    }
}
