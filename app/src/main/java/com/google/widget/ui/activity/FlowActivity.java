package com.google.widget.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.widget.utils.ToastUtil;
import com.google.widget.utils.UIUtil;
import com.google.widget.view.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/29 22:59
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class FlowActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private FlowLayout mFlowLayout;
    private List<String> mData;
    private Gson mGson;
    private String appname = "['QQ','视频','放开那三国','电子书','酒店','单机','小说','斗地主','优酷','网游','WIFI万能钥匙'," +
            "'播放器','捕鱼达人2','机票','游戏','熊出没之熊大快跑','美图秀秀','浏览器','单机游戏','我的世界','电影电视','QQ空间','旅游'," +
            "'免费游戏','2048','刀塔传奇','壁纸','节奏大师','锁屏','装机必备','天天动听','备份','网盘','海淘网','大众点评','爱奇艺视频'," +
            "'腾讯手机管家','百度地图','猎豹清理大师','谷歌地图','hao123上网导航','京东','youni有你','万年历-农历黄历','支付宝钱包']";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        mGson = new Gson();
        mData = mGson.fromJson(appname,new TypeToken<List<String>>(){}.getType());
    }

    private void initView() {
        setContentView(mScrollView = new ScrollView(this));

        SpannableString title = new SpannableString("流式布局,热门标签");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        mScrollView.setBackgroundColor(Color.parseColor("#eaeaea"));
        mScrollView.setVerticalScrollBarEnabled(false);
        mFlowLayout = new FlowLayout(this);

        int padding = UIUtil.dip2px(15);
        mFlowLayout.setPadding(UIUtil.dip2px(10), padding, UIUtil.dip2px(10), padding);
        mFlowLayout.setSpace(UIUtil.dip2px(10), UIUtil.dip2px(15));
        for (final String data : mData) {

            TextView textView = new TextView(this);
            int tvPadding = UIUtil.dip2px(10);
            textView.setPadding(UIUtil.dip2px(15), tvPadding, UIUtil.dip2px(15), tvPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setText(data);
            textView.setTextColor(Color.WHITE);

            Random random = new Random();//Math.random()
            int alpha = 255;
            int green = random.nextInt(190) + 30;
            int red = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);

            //设置shape
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setCornerRadius(UIUtil.dip2px(6));
            normalDrawable.setColor(argb);

            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setColor(Color.DKGRAY);
            pressedDrawable.setCornerRadius(UIUtil.dip2px(5));


            //设置选择器selector
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateListDrawable.addState(new int[]{}, normalDrawable);

            textView.setBackgroundDrawable(stateListDrawable);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.toast(data);
                }
            });
            mFlowLayout.addView(textView);
        }

        mScrollView.addView(mFlowLayout);
    }
}
