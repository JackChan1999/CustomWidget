package com.google.widget.test;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.reminder.GooViewListener;
import com.google.widget.view.swipe.SwipeLayout;

import java.util.HashSet;
import java.util.List;

import static com.google.widget.domain.Cheeses.NAMES;

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
public class MyAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private HashSet<Integer> mRemove = new HashSet<>();
    HashSet<SwipeLayout> mUnCloseLayout = new HashSet<>();

    public MyAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 120;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static final int[] HEAD_IDS = new int[]{
            R.mipmap.head_1,
            R.mipmap.head_2,
            R.mipmap.head_3,
    };

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_swipe,parent);
            holder = ViewHolder.fromValues(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        SwipeLayout view = (SwipeLayout) convertView;
        view.close(false,false);
        view.getFrontView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("item click: " + position);
            }
        });

        holder.mImageView.setImageResource(HEAD_IDS[position%HEAD_IDS.length]);
        holder.name.setText(NAMES[position%NAMES.length]);
        holder.del.setTag(position);
        holder.del.setOnClickListener(mOnClickListener);
        holder.call.setTag(position);
        holder.call.setOnClickListener(mOnClickListener);

        TextView mUnreadView = holder.mReminder;
        boolean visible = !mRemove.contains(position);
        mUnreadView.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible){
            mUnreadView.setText(String.valueOf(position));
            mUnreadView.setTag(position);
            GooViewListener gooViewListener = new GooViewListener(mContext,mUnreadView){
                @Override
                public void onDisappear(PointF mDragCenter) {
                    super.onDisappear(mDragCenter);
                    mRemove.add(position);
                    notifyDataSetChanged();
                    ToastUtil.toast("Cheers! We have get rid of it!");
                }

                @Override
                public void onReset(boolean isOutOfRange) {
                    super.onReset(isOutOfRange);
                    notifyDataSetChanged();
                    ToastUtil.toast("Cheers! We have get rid of it!");
                }
            };
            mUnreadView.setOnTouchListener(gooViewListener);
        }
        return view;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            switch (v.getId()){
                case R.id.bt_delete:
                    closeAllLayout();
                    ToastUtil.toast("position: " + pos + " del");
                    break;
                case R.id.bt_call:
                    ToastUtil.toast("position: " + pos + " call");
                    break;
            }
        }
    };

    private void closeAllLayout() {
        if (mUnCloseLayout.size() == 0){
            return;
        }
        for (SwipeLayout sl : mUnCloseLayout){
            sl.close(false,false);
        }
        mUnCloseLayout.clear();
    }


    private static class ViewHolder {

        ImageView mImageView;
        Button del;
        Button call;
        TextView name;
        TextView mReminder;

        private ViewHolder(ImageView imageView,Button del, Button call, TextView name, TextView reminder){
            this.mImageView = imageView;
            this.del = del;
            this.call = call;
            this.name = name;
            this.mReminder = reminder;
        }

        public static ViewHolder fromValues(View view){
            return new ViewHolder(
                    (ImageView) view.findViewById(R.id.iv_head),
                    (Button) view.findViewById(R.id.bt_delete),
                    (Button)view.findViewById(R.id.bt_call),
                    (TextView)view.findViewById(R.id.point),
                    (TextView)view.findViewById(R.id.tv_name)
            );
        }
    }
}
