package com.google.widget.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.CustomViewPager;
import com.google.widget.view.PageTabStrip;

import java.util.ArrayList;
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
public class CustomViewPagerActivity extends AppCompatActivity implements CustomViewPager
        .OnPageChangeListener {

    @Bind(R.id.viewpager)
    CustomViewPager mViewPager;
    @Bind(R.id.pageTabStrip)
    PageTabStrip mTabStrip;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer)
    DrawerLayout mDrawer;
    @Bind(R.id.lv_left_menu)
    ListView mLvLeftMenu;

    private ActionBarDrawerToggle mToggle;

    private int[] mImageIds = new int[]{R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R
            .mipmap.a5, R.mipmap.a6};
    private String[] tabText = new String[]{"图片1","图片2","图片3","图片4","图片5","图片6","图片7"};
    private ActionBar mActionBar;
    private ShareActionProvider mShareProvider;
    //private RadioGroup.LayoutParams mParams;
    //private ColorStateList mColorStateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        initDrawerLayout();

        /*SpannableString title = new SpannableString("ViewPager");
        title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(title);
        mActionBar.setElevation(0);*/

        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mViewPager.addView(image);
        }

        //添加测试页面
        View testView = LayoutInflater.from(this).inflate(R.layout.layout_test, null);
        mViewPager.addView(testView, 6);

       /* mParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, UIUtil.dip2px(48));
        mColorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked},
                new int[]{}}, new int[]{Color.WHITE,Color.BLACK});
        for (int i = 0; i < mImageIds.length + 1; i++) {
            mRadioGroup.addView(initRadioButton(i));
        }*/

        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CustomViewPagerActivity.this.onPagerSelected(0);
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mTabStrip.setViewPager(mViewPager,tabText,5,point.x);
    }

    private void initDrawerLayout(){
        mToolbar.setTitle("ViewPager");
        //mToolbar.setSubtitle("副标题");
        /*这些通过ActionBar来设置也是一样的*/
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,mToolbar,R.string.open,R.string.close);
        mToggle.syncState();
        mDrawer.addDrawerListener(mToggle);

        mLvLeftMenu.addHeaderView(LayoutInflater.from(this).inflate(R.layout.nav_header_2,mLvLeftMenu,false));
        mLvLeftMenu.setAdapter(new MenuItemAdapter(this));
    }

   /* private RadioButton initRadioButton(int position) {
        RadioButton button = new RadioButton(this);
        button.setId(position);
        button.setText("图片"+position);
        button.setTextColor(mColorStateList);
        button.setButtonDrawable(android.R.color.transparent);
        button.setTextSize(16);
        button.setPadding(100,0,100,0);
        mParams.weight =1;
        button.setLayoutParams(mParams);
        button.setGravity(Gravity.CENTER);
        if (position == 0) {
            button.setChecked(true);
        }
        return button;
    }*/

    private void initListener() {
        mViewPager.setOnPageChangeListener(this);

        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings:
                        ToastUtil.toast("action_settings");
                        break;
                    case R.id.action_share:
                        ToastUtil.toast("action_share");
                        break;
                }
                return true;
            }
        });

        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawer.closeDrawers();
            }
        });
    }

    private void colorChange(int pos) {
            if (pos == 6) pos = 0;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mImageIds[pos]);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    mToolbar.setBackgroundDrawable(new ColorDrawable(vibrant.getRgb()));
                    mTabStrip.setBackgroundColor(vibrant.getRgb());
                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setStatusBarColor(colorBurn(vibrant.getRgb()));
                        //window.setNavigationBarColor(Color.RED);
                    }
                }
            });
    }

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
    public void onPagerSelected(int pos) {
        mTabStrip.check(pos);
        colorChange(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        mShareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareProvider.setShareIntent(intent);

        SearchView searchView = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        LinearLayout linearLayout = (LinearLayout) searchView.findViewById(R.id.search_plate);
        linearLayout.setBackgroundResource(R.drawable.textfield_search_activated_mtrl_alpha);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private class LvMenuItem{

        private int type;
        private int icon;
        private String name;

        private static final int NO_ICON = 0;
        private static final int TYPE_NORMAL = 0;
        private static final int TYPE_NO_ICON = 1;
        private static final int TYPE_SEPARATOR  = 2;

        public LvMenuItem()
        {
            this(null);
        }

        public LvMenuItem(String name)
        {
            this(NO_ICON, name);
        }

        public LvMenuItem(int icon, String name){
            this.icon = icon;
            this.name = name;

            if (icon == NO_ICON && TextUtils.isEmpty(name))
            {
                type = TYPE_SEPARATOR;
            } else if (icon == NO_ICON)
            {
                type = TYPE_NO_ICON;
            } else
            {
                type = TYPE_NORMAL;
            }

            if (type != TYPE_SEPARATOR && TextUtils.isEmpty(name))
            {
                throw new IllegalArgumentException("you need set a name for a non-SEPARATOR item");
            }
        }

    }

    private class MenuItemAdapter extends BaseAdapter{

        private final int mIconSize;
        private LayoutInflater mInflater;
        private Context mContext;

        public MenuItemAdapter(Context context){
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mIconSize = context.getResources().getDimensionPixelSize(R.dimen.drawer_icon_size);
        }

        private List<LvMenuItem> mItems = new ArrayList<>(
                Arrays.asList( new LvMenuItem(),
                        new LvMenuItem(R.mipmap.ic_drawer_home_normal,"首页"),
                        new LvMenuItem(R.mipmap.ic_drawer_explore_normal,"发现"),
                        new LvMenuItem(R.mipmap.ic_drawer_follow_normal,"关注"),
                        new LvMenuItem(R.mipmap.ic_drawer_collect_normal,"收藏"),
                        new LvMenuItem(R.mipmap.ic_drawer_draft_normal,"草稿"),
                        new LvMenuItem(R.mipmap.ic_drawer_question_normal,"提问"),
                        //new LvMenuItem(),
                        new LvMenuItem("Sub Items"),
                        new LvMenuItem(R.mipmap.ic_commented,"评论"),
                        new LvMenuItem(R.mipmap.ic_nohelped,"设置"))
        );


        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LvMenuItem item = mItems.get(position);
            switch (item.type){
                case LvMenuItem.TYPE_NORMAL:
                    if (convertView == null)
                    {
                        convertView = mInflater.inflate(R.layout.design_drawer_item, parent,
                                false);
                    }
                    TextView itemView = (TextView) convertView;
                    itemView.setText(item.name);
                    Drawable icon = mContext.getResources().getDrawable(item.icon);
                    //setIconColor(icon);
                    if (icon != null)
                    {
                        icon.setBounds(0, 0, mIconSize, mIconSize);
                        TextViewCompat.setCompoundDrawablesRelative(itemView, icon, null, null, null);
                    }
                    break;

                case LvMenuItem.TYPE_NO_ICON:
                    if (convertView == null)
                    {
                        convertView = mInflater.inflate(R.layout.design_drawer_item_subheader,
                                parent, false);
                    }
                    TextView subHeader = (TextView) convertView;
                    subHeader.setText(item.name);
                    break;

                case LvMenuItem.TYPE_SEPARATOR:
                    if (convertView == null)
                    {
                        convertView = mInflater.inflate(R.layout.design_drawer_item_separator,
                                parent, false);
                    }
                    break;
            }
            return convertView;
        }

        public void setIconColor(Drawable icon)
        {
            int textColorSecondary = android.R.attr.textColorSecondary;
            TypedValue value = new TypedValue();
            if (!mContext.getTheme().resolveAttribute(textColorSecondary, value, true))
            {
                return;
            }
            int baseColor = mContext.getResources().getColor(value.resourceId);
            icon.setColorFilter(baseColor, PorterDuff.Mode.MULTIPLY);
        }
    }

}
