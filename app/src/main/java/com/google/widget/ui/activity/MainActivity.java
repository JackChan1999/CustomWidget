package com.google.widget.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.widget.R;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.recyclerview.CommonAdapter;
import com.google.widget.base.adapter.recyclerview.OnItemClickListener;
import com.google.widget.ui.fragment.CustomViewFragment;
import com.google.widget.ui.fragment.SampleFragment;
import com.google.widget.utils.ToastUtil;
import com.google.widget.utils.UIUtil;
import com.google.widget.view.PromotedActions;

import java.util.ArrayList;
import java.util.Arrays;
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
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String[] view_name;
    private List<String> mMenuData;
    private MenuAdapter mAdapter;
    private static Sample[] mSamples;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;
    private FrameLayout mFlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
       /* initData();
        setAdapter();
        initSamples();
        initListener();*/
        initPromotedActions();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        //mRecyclerView = (RecyclerView) findViewById(R.id.menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("自定义控件");
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawer,toolbar,R.string.open,R.string.close);
        mToggle.syncState();
        mDrawer.addDrawerListener(mToggle);

        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        if (nav != null){
            setupDrawerContent(nav);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }

        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
    }

    public void initPromotedActions(){
        mFlContainer = (FrameLayout) findViewById(R.id.container);
        PromotedActions actions = new PromotedActions();
        actions.setup(this, mFlContainer);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case 0:
                        ToastUtil.toast("娱乐...");
                        break;
                    case 1:
                        ToastUtil.toast("电影...");
                        break;
                    case 2:
                        ToastUtil.toast("音乐...");
                        break;
                    case 3:
                        ToastUtil.toast("脱口秀...");
                        break;
                    case 4:
                        ToastUtil.toast("科技...");
                        break;
                }
            }
        };
        int colors[] = new int[]{0xff59a2be,0xffad62a7,0xfff16364,0xfff9a43e,0xff67bf74};
        int resId[] = new int[]{R.mipmap.channel2,R.mipmap.channel3,R.mipmap.channel5,
                R.mipmap.channel7,R.mipmap.ic_add_white_24dp};
        for (int i=0; i<resId.length; i++){
            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}},new int[]{colors[i]});
            FloatingActionButton button = getButton();
            button.setImageResource(resId[i]);
            button.setBackgroundTintList(colorStateList);
            button.setRippleColor(Color.RED);
            button.setId(i);
            if (i == resId.length - 1){
                button.setVisibility(View.VISIBLE);
                actions.addMainItem(button);
            }else {
                button.setVisibility(View.GONE);
                actions.addItem(button, listener);
            }
        }
    }

    public FloatingActionButton getButton(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UIUtil.dip2px(45),UIUtil.dip2px(45));
        params.bottomMargin = UIUtil.dip2px(50);
        params.rightMargin = UIUtil.dip2px(40);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        FloatingActionButton button = new FloatingActionButton(this);
        button.setLayoutParams(params);
        int padding = UIUtil.dip2px(10);
        button.setPadding(padding,padding,padding,padding);
        return button;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CustomViewFragment(),"选项卡1");
        adapter.addFragment(new SampleFragment(),"选项卡2");
        adapter.addFragment(new SampleFragment(),"选项卡3");
        viewPager.setAdapter(adapter);
    }

    private void initData() {
        view_name = UIUtil.getStringArray(R.array.view_name);
        mMenuData = Arrays.asList(view_name);
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MenuAdapter(this, R.layout.item_menu, mMenuData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                startActivity(new Intent(MainActivity.this, mSamples[position].activityClass));
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void setupDrawerContent(NavigationView nav) {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mDrawer.closeDrawers();
                return true;
            }
        });
    }

    public FrameLayout getFrameLayout(){
        return mFlContainer;
    }

    public int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = mFlContainer.getChildAt(0).getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    private class MenuAdapter extends CommonAdapter<String> {

        public MenuAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.tv_item, s);
        }
    }

    private void initSamples(){
        mSamples = new Sample[]{
                new Sample(R.string.app_name,RotateMenuActivity.class),
                new Sample(R.string.app_name,BannerActivity.class),
                new Sample(R.string.app_name,CustomViewPagerActivity.class),
                new Sample(R.string.app_name,CustomSpinnerActivity.class),
                new Sample(R.string.app_name,SwitchActivity.class),
                new Sample(R.string.app_name,PullRefreshActivity.class),
                new Sample(R.string.app_name,ParallaxActivity.class),
                new Sample(R.string.app_name,ContactActivity.class),
                new Sample(R.string.app_name,RingWaveActivity.class),
                new Sample(R.string.app_name,FlowActivity.class),
                new Sample(R.string.app_name,SlidingMenuActivity.class),
                new Sample(R.string.app_name,DragActivity.class),
                //SweepActivity.class
                new Sample(R.string.app_name,SwipeActivity.class),
                new Sample(R.string.app_name,GooViewActivity.class),
                new Sample(R.string.app_name,QQDemoActivity.class),
                new Sample(R.string.app_name,ProgressButtonActivity.class),
                new Sample(R.string.app_name,CPBAcitivity.class),
                new Sample(R.string.app_name,RatioActivity.class),
                new Sample(R.string.app_name,HeaderTabActivity.class),
                new Sample(R.string.app_name,ScreenSlidePagerActivity.class),
                new Sample(R.string.app_name,MDActivity.class),
                new Sample(R.string.app_name,StaggeredGridAct.class)
        };
    }

    private class Sample {
        private CharSequence title;
        private Class<? extends Activity> activityClass;

        public Sample(int titleResId, Class<? extends Activity> activityClass){
            this.title = getResources().getString(titleResId);
            this.activityClass = activityClass;
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }

    private void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private class Adapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            mFragments.add(fragment);
            mTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
