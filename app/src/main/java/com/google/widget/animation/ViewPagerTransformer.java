package com.google.widget.animation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/4 09:15
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class ViewPagerTransformer extends ViewPager{

    private SparseArray<ImageView> mHashMap;
    private View mLeft;
    private View mRight;
    private float mTranslationX = 0;
    private static final float MIN_SCALE = 0.75f;
    private  float mScaleX = 0;

    public ViewPagerTransformer(Context context) {
        this(context, null);
    }

    public ViewPagerTransformer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHashMap = new SparseArray<>();
    }

    public void addChildView(int position, ImageView image){
        mHashMap.put(position,image);
    }

    public void removeChildView(int position, ImageView image){
        mHashMap.remove(position);
    }


    /**
     * @param position 位置
     * @param offset 比例0-1
     * @param offsetPixels 像素
     */
    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        mLeft = mHashMap.get(position);
        mRight = mHashMap.get(position+1);
        startAnimation(mLeft,mRight,position,offset,offsetPixels);
        super.onPageScrolled(position, offset, offsetPixels);
    }

    private void startAnimation(View left, View right, int position, float offset, int offsetPixels) {
        if (right != null){
            mTranslationX = -getWidth() + offsetPixels;
            right.setTranslationX(mTranslationX);
            mScaleX = (1-MIN_SCALE)*offset + MIN_SCALE;
            right.setScaleX(mScaleX);
            right.setScaleY(mScaleX);
        }

        if (left != null){
            left.bringToFront();
        }
    }
}
