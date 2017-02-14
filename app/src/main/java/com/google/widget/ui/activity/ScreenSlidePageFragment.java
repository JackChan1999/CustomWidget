package com.google.widget.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.widget.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/3 08:43
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class ScreenSlidePageFragment extends Fragment {

    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private int colors[] = new int[]{Color.parseColor("#8f46b4"),Color.parseColor("#7ad76f"),
            Color.parseColor("#4ec0d7"),Color.parseColor("#d7ce4e"),Color.parseColor("#e1855e")};

    public static ScreenSlidePageFragment create(int number){
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ((TextView) rootView.findViewById(R.id.tv)).setText(mPageNumber+"");
        ((LinearLayout) rootView.findViewById(R.id.content)).setBackgroundColor(colors[mPageNumber]);
        return rootView;
    }

}
