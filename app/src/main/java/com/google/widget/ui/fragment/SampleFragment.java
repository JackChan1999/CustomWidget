package com.google.widget.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.widget.R;
import com.google.widget.ui.activity.DetailActivity;
import com.google.widget.utils.Cheeses2;

import java.util.ArrayList;
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
public class SampleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_list,container,false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new RecyclerViewAdapter(getActivity(),getRandomSublist(Cheeses2.sCheeseStrings,30)));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1,parent,false);
            view.setBackgroundResource(mBackGround);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context,DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_NAME,holder.mString);
                    context.startActivity(intent);
                }
            });
            Glide.with(holder.mImageView.getContext()).load(Cheeses2.getRandomCheeseDrawable())
                    .fitCenter().into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{

            private View mView;
            private ImageView mImageView;
            private TextView mTextView;
            private String mString;


            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mImageView = (ImageView) itemView.findViewById(R.id.avatar);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }
    }
}
