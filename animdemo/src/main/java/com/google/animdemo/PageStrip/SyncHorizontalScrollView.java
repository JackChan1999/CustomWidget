package com.google.animdemo.PageStrip;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/5 13:21
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.animdemo.R;

public class SyncHorizontalScrollView extends HorizontalScrollView {

    private View view;
    private ImageView leftImage;
    private ImageView rightImage;
    private int windowWitdh = 0;
    private Activity mContext;
    private RadioGroup rg_nav_content;
    private ImageView iv_nav_indicator;
    private LayoutInflater mInflater;
    private int count;// 屏幕显示的标签个数
    private int indicatorWidth;// 每个标签所占的宽度
    private int currentIndicatorLeft = 0;// 当前所在标签页面的位移
    private ViewPager mViewPager;
    private int scrollX;

    public SyncHorizontalScrollView(Context context) {
        super(context);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * 方法描述：
     * @param mViewPager
     * @param leftImage 左箭頭
     * @param rightImage 右箭頭
     * @param tabTitle 標籤欄的名稱
     * @param count 一頁顯示的標籤個數
     * @param context
     * <pre>
     * 修改日期      修改人       修改说明
     * 2014-2-17   chen     新建
     * </pre>
     */
    public void setSomeParam(ViewPager mViewPager, ImageView leftImage,
                             ImageView rightImage, String[] tabTitle, int count, Activity context) {
        this.mContext = context;
        this.mViewPager = mViewPager;
        // this.view = view;
        mInflater = LayoutInflater.from(context);
        this.view = mInflater.inflate(R.layout.layout_page_tab_strip, null);
        this.addView(view);
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWitdh = dm.widthPixels;
        this.count = count;
        indicatorWidth = windowWitdh / count;
        init(tabTitle);
    }

    private void init(String[] tabTitle) {
        rg_nav_content = (RadioGroup) view.findViewById(R.id.rg_nav_content);
        iv_nav_indicator = (ImageView) view.findViewById(R.id.iv_nav_indicator);
        initIndicatorWidth();
        initNavigationHSV(tabTitle);
        setListener();
    }

    // 初始化滑动下标的宽
    private void initIndicatorWidth() {
        ViewGroup.LayoutParams cursor_Params = iv_nav_indicator
                .getLayoutParams();
        cursor_Params.width = indicatorWidth;
        iv_nav_indicator.setLayoutParams(cursor_Params);
    }

    // 添加顶部标签
    private void initNavigationHSV(String[] tabTitle) {
        rg_nav_content.removeAllViews();
        for (int i = 0; i < tabTitle.length; i++) {
            RadioButton rb = (RadioButton) mInflater.inflate(
                    R.layout.layout_rb, null);
            rb.setId(i);
            rb.setText(tabTitle[i]);
            rb.setLayoutParams(new LayoutParams(indicatorWidth,
                    LayoutParams.MATCH_PARENT));
            rg_nav_content.addView(rb);
        }
        RadioButton rb = (RadioButton) mInflater.inflate(
                R.layout.layout_rb, null);
        rg_nav_content.addView(rb);

    }

    private void setListener() {
        rg_nav_content
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (rg_nav_content.getChildAt(checkedId) != null) {
                            moveAnimation(checkedId);
                            mViewPager.setCurrentItem(checkedId); // ViewPager
                            // 跟随一起 切换

                        }
                    }
                });
    }
    //动画移动效果
    private void moveAnimation(int checkedId){
        TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft,
                indicatorWidth * checkedId,0f, 0f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        // 执行位移动画
        iv_nav_indicator.startAnimation(animation);

        // 记录当前 下标的距最左侧的 距离
        currentIndicatorLeft = indicatorWidth * checkedId;
        scrollX = (checkedId > 1 ? currentIndicatorLeft: 0)- indicatorWidth * 2;
        this.post(runnable);
    }

    // 模拟点击事件
    public void performLabelClick(int position) {
        if (rg_nav_content != null && rg_nav_content.getChildCount() > position) {
            ((RadioButton) rg_nav_content.getChildAt(position)).performClick();
        }
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            smoothScrollTo(scrollX, 0);
        }
    };

    // 显示和隐藏左右两边的箭头
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!mContext.isFinishing() && view != null && rightImage != null
                && leftImage != null) {
            if (view.getWidth() <= windowWitdh) {
                leftImage.setVisibility(View.GONE);
                rightImage.setVisibility(View.GONE);
            } else {
                if (l == 0) {
                    leftImage.setVisibility(View.GONE);
                    rightImage.setVisibility(View.VISIBLE);
                } else if (view.getWidth() - l == windowWitdh) {
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.GONE);
                } else {
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public int getIndicatorWidth(){
        return indicatorWidth;
    }
}
