package com.google.widget.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.widget.R;
/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-4-16 下午10:48:27
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class TabActionBarView extends LinearLayout implements View.OnClickListener {

    private static final int LEFT_TAB_INDEX = 0;
    private static final int MIDDLE_TAB_INDEX = 1;
    private static final int RIGHT_TAB_INDEX = 2;

    private int mSelectTabIndex = -1;
    private TextView mLeftTextView;
    private TextView mMiddleTextView;
    private TextView mRightTextView;
    private int mTextSelectedColor;
    private int mTextNormalColor;
    private ITabActionCallback mCallback;

    public TabActionBarView(Context context) {
        this(context, null, 0);
    }

    public TabActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabActionBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mTextNormalColor = context.getResources().getColor(R.color.white);
        mTextSelectedColor = context.getResources().getColor(R.color.green_dark);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        View view = LayoutInflater.from(context).inflate(R.layout.action_item_tab, this, true);
        mLeftTextView = (TextView) view.findViewById(R.id.action_tab_left);
        mMiddleTextView = (TextView) view.findViewById(R.id.action_tab_middle);
        mRightTextView = (TextView) view.findViewById(R.id.action_tab_right);
    }

    public void bindTab(ITabActionCallback callback, String leftText, String rightText) {
        bindTab(callback, leftText, null, rightText);
    }

    public void bindTab(ITabActionCallback callback, String leftText, String middleText, String rightText) {
        mCallback = callback;
        mLeftTextView.setText(leftText);
        mLeftTextView.setOnClickListener(this);

        mRightTextView.setText(rightText);
        mRightTextView.setOnClickListener(this);

        if (TextUtils.isEmpty(middleText))
            mMiddleTextView.setVisibility(View.GONE);
        else {
            mMiddleTextView.setVisibility(View.VISIBLE);
            mMiddleTextView.setText(middleText);
            mMiddleTextView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.action_tab_left:
                if (mSelectTabIndex == LEFT_TAB_INDEX)
                    return;

                leftClick();
                break;

            case R.id.action_tab_middle:
                if (mSelectTabIndex == MIDDLE_TAB_INDEX)
                    return;

                middleClick();
                break;

            case R.id.action_tab_right:
                if (mSelectTabIndex == RIGHT_TAB_INDEX)
                    return;

                rightClick();
                break;
        }
    }

    void cleanPreviousStyle() {
        switch (mSelectTabIndex) {

            case LEFT_TAB_INDEX:
                mLeftTextView.setBackgroundResource(R.mipmap.tab_left_normal);
                mLeftTextView.setTextColor(mTextNormalColor);
                break;
            case MIDDLE_TAB_INDEX:
                mMiddleTextView.setBackgroundResource(R.mipmap.tab_middle_normal);
                mMiddleTextView.setTextColor(mTextNormalColor);
                break;
            case RIGHT_TAB_INDEX:
                mRightTextView.setBackgroundResource(R.mipmap.tab_right_normal);
                mRightTextView.setTextColor(mTextNormalColor);
                break;
        }
    }

    public void leftClick() {
        cleanPreviousStyle();
        mLeftTextView.setBackgroundResource(R.mipmap.tab_left_select);
        mLeftTextView.setTextColor(mTextSelectedColor);
        mCallback.onLeftTabClick();

        mSelectTabIndex = LEFT_TAB_INDEX;
    }

    public void middleClick() {
        cleanPreviousStyle();
        mMiddleTextView.setBackgroundResource(R.mipmap.tab_middle_select);
        mMiddleTextView.setTextColor(mTextSelectedColor);
        mCallback.onMiddleTabClick();

        mSelectTabIndex = MIDDLE_TAB_INDEX;
    }

    public void rightClick() {
        cleanPreviousStyle();
        mRightTextView.setBackgroundResource(R.mipmap.tab_right_select);
        mRightTextView.setTextColor(mTextSelectedColor);
        mCallback.onRightClick();

        mSelectTabIndex = RIGHT_TAB_INDEX;
    }

    public interface ITabActionCallback {
        void onLeftTabClick();

        void onMiddleTabClick();

        void onRightClick();
    }
}
