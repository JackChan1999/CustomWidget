package com.google.widget.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.base.adapter.recyclerview.OnItemClickListener;
import com.google.widget.utils.UIUtil;
import com.google.widget.view.ViewPagerScroller;

import java.lang.reflect.Field;
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
public class BannerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.viewpager)
    public ViewPager mViewPager;
    @Bind(R.id.tvTitle)
    public TextView mTextView;
    @Bind(R.id.llcontainer)
    public LinearLayout mContainer;
    @Bind(R.id.content)
    public LinearLayout mContent;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private int mPreviousPos;
    private int[] imgs = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};
    private String[] title;
    private ArrayList<String> transformerList = new ArrayList<>();
    private RvAdapter mRvAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++currentItem);
            mHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };
    private ViewPagerScroller mScroller;
    private ViewPagerScroller scroller;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initRecyclerView();
        initListener();
        initTransformer();
        initViewPagerScroll();
    }

    private void initView() {
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);
        title = UIUtil.getStringArray(R.array.title);

        SpannableString actionBarTitle = new SpannableString("大图轮播");
        actionBarTitle.setSpan(new ForegroundColorSpan(Color.WHITE), 0, actionBarTitle.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(actionBarTitle);

        mViewPager.setAdapter(new Adapter());
        int middle = Integer.MAX_VALUE / 2;
        int extra = middle % imgs.length;
        int currentItem = middle - extra;
        mViewPager.setCurrentItem(currentItem);
        mHandler.sendEmptyMessageDelayed(0, 5000);

        for (int i = 0; i < imgs.length; i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.leftMargin = 10;
                img.setEnabled(false);
            }
            img.setLayoutParams(params);
            mContainer.addView(img);
        }
        mTextView.setText(title[0]);

       mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BannerActivity.this.onPageSelected(0);
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRvAdapter = new RvAdapter(this, R.layout.list_item, transformerList);
        mRecyclerView.setAdapter(mRvAdapter);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
        /*new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % imgs.length;
                mTextView.setText(title[pos]);
                mContainer.getChildAt(pos).setEnabled(true);
                mContainer.getChildAt(mPreviousPos).setEnabled(false);
                mPreviousPos = pos;
                colorChange(pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }*/

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0, 5000);
                        break;
                }
                return false;
            }
        });

        mRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                String transforemerName = transformerList.get(position);
                try {
                    Class clazz = Class.forName("com.ToxicBakery.viewpager.transforms." +
                            transforemerName);
                    ABaseTransformer transformer = (ABaseTransformer) clazz.newInstance();
                    mViewPager.setPageTransformer(true, transformer);
                    if (transforemerName.equals("StackTransformer")) {
                        scroller.setScrollDuration(1200);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        Field mScroller = null;
        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initTransformer() {
        //各种翻页效果
        transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        mRvAdapter.notifyDataSetChanged();
    }

    private void colorChange(int pos) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[pos]);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                mContent.setBackgroundColor(toARGB(vibrant.getRgb()));
                mTextView.setTextColor(vibrant.getBodyTextColor());
                mActionBar.setBackgroundDrawable(new ColorDrawable(vibrant.getRgb()));

                if (Build.VERSION.SDK_INT >=21){
                    Window window = getWindow();
                    window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    //window.setNavigationBarColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int pos = position % imgs.length;
        mTextView.setText(title[pos]);
        mContainer.getChildAt(pos).setEnabled(true);
        if (pos != mPreviousPos){
            mContainer.getChildAt(mPreviousPos).setEnabled(false);
        }
        mPreviousPos = pos;
        colorChange(pos);
    }

    /**
     * 颜色添加透明度
     * @param rgb
     * @return
     */
    protected  static  int toARGB(int rgb){
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        return Color.argb(180 , red , green , blue );
    }

    /**
     * 颜色加深处理
     * @param RGBValues
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (imgs.length != 0) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int pos = position % imgs.length;
            ImageView img = new ImageView(BannerActivity.this);
            img.setBackgroundResource(imgs[pos]);
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class RvAdapter extends CommonAdapter<String> {

        public RvAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.tv_item, s);
        }
    }
}
