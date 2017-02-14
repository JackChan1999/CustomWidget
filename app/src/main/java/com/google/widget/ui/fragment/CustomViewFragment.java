package com.google.widget.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.ui.activity.BannerActivity;
import com.google.widget.ui.activity.CPBAcitivity;
import com.google.widget.ui.activity.ContactActivity;
import com.google.widget.ui.activity.CustomSpinnerActivity;
import com.google.widget.ui.activity.CustomViewPagerActivity;
import com.google.widget.ui.activity.DetailActivity;
import com.google.widget.ui.activity.DragActivity;
import com.google.widget.ui.activity.FlowActivity;
import com.google.widget.ui.activity.GooViewActivity;
import com.google.widget.ui.activity.HeaderTabActivity;
import com.google.widget.ui.activity.ParallaxActivity;
import com.google.widget.ui.activity.ProgressButtonActivity;
import com.google.widget.ui.activity.PullRefreshActivity;
import com.google.widget.ui.activity.QQDemoActivity;
import com.google.widget.ui.activity.QQHealthViewActivity;
import com.google.widget.ui.activity.RatioActivity;
import com.google.widget.ui.activity.RingWaveActivity;
import com.google.widget.ui.activity.RotateMenuActivity;
import com.google.widget.ui.activity.ScreenSlidePagerActivity;
import com.google.widget.ui.activity.SlidingMenuActivity;
import com.google.widget.ui.activity.SpeedControlActivity;
import com.google.widget.ui.activity.StaggeredGridAct;
import com.google.widget.ui.activity.SwipeActivity;
import com.google.widget.ui.activity.SwitchActivity;
import com.google.widget.utils.ColorGenerator;
import com.google.widget.utils.UIUtil;
import com.google.widget.view.TextDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/8 09:55
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class CustomViewFragment extends Fragment{

    private SparseArray<TextDrawable> array;
    private List<String> list;
    private ColorGenerator mGenerator = ColorGenerator.DEFAULT;
    private static Sample[] mSamples;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_list,container,false);
        setupRecyclerView(rv);
        initSamples();
        return rv;
    }

    private void setupRecyclerView(RecyclerView rv) {
        list = Arrays.asList(UIUtil.getStringArray(R.array.view_name));
        array = new SparseArray<>(list.size());
        for (int i=0; i<list.size(); i++){
            String text = list.get(i).substring(0,1);
            array.put(i,TextDrawable.builder().buildRound(text,mGenerator.getColor(text)));
        }
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new RecyclerViewAdapter(getActivity(),list));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
           /* boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
            if (isSignificantDelta) {
                if (dy > 0) {

                } else {

                }
            }*/
        }
    };

    public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        private final TypedValue mValue = new TypedValue();
        private int mBackGround;
        private List<String> mValues;

        public RecyclerViewAdapter(Context context, List<String> items){
            //获取attr属性值
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,mValue,true);
            mBackGround = mValue.resourceId;
            mValues = items;
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2,parent,false);
            view.setBackgroundResource(mBackGround);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
          holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder{

            private View mView;
            private ImageView mImageView;
            private TextView mTextView;
            private String mString;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mImageView = (ImageView) itemView.findViewById(R.id.icon);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }

            public void setData(final int position) {
                mString = mValues.get(position);
                mTextView.setText(mValues.get(position));
                mImageView.setImageDrawable(array.get(position));
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), mSamples[position].activityClass));
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }
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
                new Sample(R.string.app_name,DetailActivity.class),
                new Sample(R.string.app_name,StaggeredGridAct.class),
                new Sample(R.string.app_name,QQHealthViewActivity.class),
                new Sample(R.string.app_name,SpeedControlActivity.class)
        };
    }
}
