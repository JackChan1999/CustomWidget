package com.google.widget.test;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.widget.R;
import com.google.widget.utils.Utils;
import com.google.widget.view.reminder.GooView;


/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/1 17:07
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class MyGooViewListener implements View.OnTouchListener,GooView.OnDisappearListener {

    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;
    private Context mContext;
    private GooView mGooView;
    private View point;
    private int number;
    private Handler mHandler;

    public MyGooViewListener(Context context, View point){
        this.mContext = context;
        this.point = point;
        this.number = (int) point.getTag();

        mManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSLUCENT;
        mGooView = new GooView(context);
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public void onDisappear(PointF mDragCenter) {
        if (mManager != null && mGooView.getParent() != null){
            mManager.removeView(mGooView);
            //播放气泡爆炸的动画
            ImageView image = new ImageView(mContext);
            image.setBackgroundResource(R.drawable.anim_bubble_pop);
            AnimationDrawable anim = (AnimationDrawable) image.getDrawable();

            final BubbleLayout bubble = new BubbleLayout(mContext);
            bubble.setCenter((int) mDragCenter.x, (int) (mDragCenter.y - Utils.getStatusBarHeight(mGooView)));
            bubble.addView(image,new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT));
            mManager.addView(bubble,mParams);

            anim.start();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mManager.removeView(bubble);
                }
            }, 500);
        }
    }

    @Override
    public void onReset(boolean isOutOfRange) {
        if (mManager != null && mGooView.getParent() != null){
            mManager.removeView(mGooView);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN){
            v.getParent().requestDisallowInterceptTouchEvent(true);
            point.setVisibility(View.INVISIBLE);

            mGooView.setNumber(number);
            mGooView.initCenter(event.getRawX(),event.getRawY());
            mGooView.setStatusBarHeight(Utils.getStatusBarHeight(v));
            mGooView.setOnDisappearListener(this);

            mManager.addView(mGooView,mParams);
        }
        mGooView.onTouchEvent(event);
        return true;
    }
}

